package sample;

import javafx.scene.image.ImageView;
import javafx.scene.shape.Line;
import javafx.scene.shape.Path;

import java.util.Vector;

public class Call
{
    Noeud distination ;
    Noeud Reciption  ;
    Vector<Line> liseLine ;
    ImageView imageView ;

    public Call(Noeud distination, Noeud reciption , Vector<Line> liseLine, ImageView imageView) {
        this.distination = distination;
        Reciption = reciption;
        this.liseLine =liseLine ;
        this.imageView = imageView ;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {

        this.imageView = imageView;
    }

    public Vector<Line> getLiseLine() {
        return liseLine;
    }

    public void setLiseLine(Vector<Line> liseLine) {
        this.liseLine = liseLine;
    }

    public Noeud getDistination() {
        return distination;
    }

    public void setDistination(Noeud distination) {
        this.distination = distination;
    }

    public Noeud getReciption() {
        return Reciption;
    }

    public void setReciption(Noeud reciption) {
        Reciption = reciption;
    }

}
