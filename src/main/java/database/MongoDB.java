package database;


import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Field;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import util.Pair;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class MongoDB implements Database {
    private MongoClient mongoClient = new MongoClient();
    private MongoDatabase database = mongoClient.getDatabase("RodsAndHarpoonsDB");

    private void removeCollection(String collection) {
        database.getCollection(collection).drop();
    }
    public void saveScores(List<Pair<String,Integer>> nicksAndScores) {
        Collections.sort(nicksAndScores, (a, b) -> -Integer.compare(a.getSecond(),b.getSecond()));
        ArrayList<Document> players = new ArrayList<>();

        for(var v : nicksAndScores)
            players.add(new Document("nickname",v.getFirst()).append("score",v.getSecond()));

        MongoCollection<Document> collection  = database.getCollection("highscores");
        collection.insertOne(new Document("players",players));
    }


    public ArrayList<ArrayList<Pair<String,Integer> > > getScoresSortedByMaxScore(int limit) {
        MongoCollection<Document> collection  = database.getCollection("highscores");
        ArrayList<ArrayList<Pair<String,Integer>>> res = new ArrayList<>();

        for(Document doc : collection.find().limit(limit)) {
            List<Document> players = doc.getList("players", Document.class);
            ArrayList<Pair<String,Integer>> nicksAndScores = new ArrayList<>();
            for(Document player: players)
                nicksAndScores.add(new Pair<>(player.getString("nickname"), player.getInteger("score")));

            res.add(nicksAndScores);
        }
        return res;

    }


    public static void main(String[] args) {
        MongoDB mongo = new MongoDB();
        mongo.removeCollection("highscores");
        List<Pair<String,Integer>> scores = Arrays.asList(new Pair<>("p1",30), new Pair<>("p2",15));
        List<Pair<String,Integer>> scores2 = Arrays.asList(new Pair<>("p3",40), new Pair<>("p4",10));
        List<Pair<String,Integer>> scores3 = Arrays.asList(new Pair<>("p5",25), new Pair<>("p26",20));
        mongo.saveScores(scores);
        mongo.saveScores(scores2);
        mongo.saveScores(scores3);
        for(var it : mongo.getScoresSortedByMaxScore(2)) {
            System.out.println("scores");
            for(var p : it) {
                System.out.println(p.getFirst()+" "+p.getSecond());
            }

        }
    }

}
