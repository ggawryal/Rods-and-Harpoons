package database;


import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import org.bson.types.ObjectId;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import util.Pair;
import util.GameInfo;

import java.util.*;


public class MongoDB implements Database {
    private MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb+srv://admin:admin@cluster0-gx7zn.mongodb.net"));
    private MongoDatabase database = mongoClient.getDatabase("RodsAndHarpoonsDB");

    private void removeCollection(String collection) {
        database.getCollection(collection).drop();
    }
    private void removeMatchesAndPlayers() {
        removeCollection("matches");
        removeCollection("players");
    }

    public ArrayList<Pair<String,Integer>> getScoresSortedByMaxScore(int limit) {
        ArrayList<Pair<String,Integer>> res = new ArrayList<>();
        MongoCollection<Document> players = database.getCollection("players");
        FindIterable<Document> cursor = players.find().sort(new BasicDBObject("highscore", -1)).limit(limit);

        for (Document document : cursor) {
            String nickName = document.getString("nickname");
            int highScore = document.getInteger("highscore");
            res.add(new Pair<>(nickName, highScore));
        }
        return res;
    }

    public void saveFinishedGame(GameInfo gameInfo) {
        gameInfo.getPlayers().sort((a, b) -> -Integer.compare(a.getPoints(), b.getPoints()));

        MongoCollection<Document> collection = database.getCollection("matches");
        Document document = gameInfo.toDocument();
        collection.insertOne(document);

        ObjectId matchId = document.get("_id", ObjectId.class);

        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions()
                .upsert(true);
        MongoCollection<Document> players = database.getCollection("players");
        players.createIndex(Indexes.ascending("nickname"), new IndexOptions().unique(true));
        for(var player : gameInfo.getPlayers()) {
            BasicDBObject query = new BasicDBObject("nickname", player.getNickname());
            BasicDBObject MatchesUpdate = new BasicDBObject("matches", matchId);
            BasicDBObject HighScoreUpdate = new BasicDBObject("highscore", player.getPoints());
            BasicDBObject update = new BasicDBObject("$addToSet", MatchesUpdate)
                    .append("$max", HighScoreUpdate);
            players.findOneAndUpdate(query, update, options);
        }
    }


    public static void main(String[] args) {
        MongoDB mongo = new MongoDB();
        for (var pair : mongo.getScoresSortedByMaxScore(5)) {
            System.out.println(pair.getFirst() + " " + pair.getSecond());
        }
    }

}
