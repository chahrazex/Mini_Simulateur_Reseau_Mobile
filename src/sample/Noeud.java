package sample;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class Noeud
{
    int id ,x ,y,sb ;
    ImageView imageView ;

    public Noeud(int id, int x, int y)
    {
        this.id = id;
        this.x = x;
        this.y = y;
        setImageView();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSb() {
        return sb;
    }

    public void setSb(int sb) {
        this.sb = sb;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView()
    {
        if (id/.2 == 0)
        {
            this.imageView = new ImageView(new Image("/ressources/person2.png",20, 50, false, false)) ;

        }
        else
        {
            this.imageView = new ImageView(new Image("/ressources/person4.png",20, 50, false, false)) ;
        }
        setXYImage();
    }
    public void  setXYImage()
    {
        imageView.setX(x-10);
        imageView.setY(y-25);
    }
}
