package board.drawable.tile;

import javafx.scene.image.Image;

import java.util.HashMap;

public class TileImageLoader {
    HashMap<String,Image> imageMap = new HashMap<>();
    public Image get(String name) {
        if(!imageMap.containsKey(name))
            imageMap.put(name,new Image(name));
        return imageMap.get(name);
    }
}
