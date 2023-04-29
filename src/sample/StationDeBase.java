package sample;

import javafx.scene.image.Image;

import java.util.Vector;

public class StationDeBase
{
    int x , y , id ;
    Vector<Noeud> listeNoeud = new Vector<>() ;
    Image image ;

    public StationDeBase(int x, int y, int id)
    {
        this.x = x;
        this.y = y;
        this.id = id;
        this.image = new Image("/ressources/bts.png",80,90,false,false);
    }
}
