
/**
 *Chahrazed Benerbbouh
 * Master 1  -RSD
 * Mini-Simulateur Réseau mobile
 */
package sample;

import com.jfoenix.controls.*;
import com.jfoenix.effects.JFXDepthManager;
import javafx.animation.PathTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Vector;

public class Reseau implements Initializable
{
    @FXML
    JFXButton genererReseauBtn ,clearBtn, exitBtn , saveBtn ;
    @FXML
    JFXTextField tailleZoneTextField , nombreNoeudTextField ;
    @FXML
    JFXComboBox nombreStationComboBox ;
    @FXML
    Pane simulationPane ,hidePane ;
    @FXML
    JFXDepthManager manager ;
    @FXML
    ImageView node ,sb ;
    @FXML
    JFXSlider mobilitySpeed ;
    @FXML
    AnchorPane anchorPane ;
    @FXML
    VBox vbox ;
    Text toast ;
    JFXSnackbar snackbar ;




    public static  int tailleZone , nombreStation , nombreNoeud ,tailleStation ,NbStationLign ;
    public static Noeud Node1 ;
    public static Vector<StationDeBase> listeSB ;//La liste qui contient les station des base
    public static Vector<Noeud> listeNoeuds ;//La liste qui contient les Noeud
    public static File file = new File("fichier.txt") ;
    public static  boolean isSeleted =false ;//variable pour tester si noued déja sélectioner pour etablir appel
    public static Vector<Line>listeLine = new Vector<>() ;
    public static   boolean stopWork = false ;//Pour arreter le thread en cour
    public  static  Vector <Call>listAppel = new Vector<>() ;


    public  static  int  nbAppel  ;




    public void genereReseau()
    {
        ValidationSupport validationSupport = new ValidationSupport();
        validationSupport.registerValidator(tailleZoneTextField, Validator.createEmptyValidator("Zone size is Required")) ;
        validationSupport.registerValidator(nombreNoeudTextField, Validator.createEmptyValidator("Number of nodes is Required")) ;
        validationSupport.registerValidator(nombreStationComboBox, Validator.createEmptyValidator("Choose item..! ")) ;

        //Si tous les champs ne sont pas vide
        if (!tailleZoneTextField.getText().isEmpty() &&
                !nombreNoeudTextField.getText().isEmpty() &&
                  !nombreStationComboBox.getSelectionModel().isEmpty())
        {

            tailleZone = Integer.parseInt(tailleZoneTextField.getText());//récupérer la taille de zone depuis le text field
            nombreNoeud = Integer.parseInt(nombreNoeudTextField.getText());
            nombreStation = Integer.parseInt(nombreStationComboBox.getValue().toString());

            /*---------just for the design !--------------------*/
            hidePane.setVisible(false);
            simulationPane.getChildren().clear();
            simulationPane.setPrefWidth(tailleZone+10);
            simulationPane.setPrefHeight(tailleZone+25);
            simulationPane.getChildren().add(new ImageView(new Image("/ressources/Background.png",
                    tailleZone+10, tailleZone+25,
                    false, false)));



            /*---------créer les Station de base -----------*/
            listeSB = new Vector<>();
            CreateStations();

            /*------créer les Noeuds-------*/
            listeNoeuds = new Vector<>();
            CreateNode();

            /*------puis positionner das la zone-------*/
            positionnerNoeud() ;

            /*------ en fin dessiner les Noeud et les station dans l'interface---------*/
            DessinerNoeud(); ;
            DessinerStation();

            /*---afficher un message de succès ----*/
            snackbar = new JFXSnackbar(vbox) ;
            toast = new Text("Network Successfully generated") ;
            toast.setStyle("-fx-fill: white");
            snackbar.enqueue( new JFXSnackbar.SnackbarEvent(toast));
        }
    }

    /*-------------------------------------------*/
    public  void  positionnerNoeud()
    {
        switch (nombreStation)
        {
            case 4:
                PositionerStationFour();
                break;//Dans le cas ou il'y 4 Station de Base
            case 9:
                PositionerStationNine();
                break;//Dans le cas ou il'y 9 Station de Base
            case 16:
                PositionnerStationSixteen();
                break;//Dans le cas ou il'y 16 Station de Base
        }
    }
    /*-------------------------------------------*/
    public void CreateStations ()
    {

        tailleStation = (int) (tailleZone / Math.sqrt(nombreStation));

        int XStation = tailleStation/2 ;
        int YStation = tailleStation/2 ;
        int count =0 ;
        NbStationLign = (int) Math.sqrt(nombreStation);

        for (int i = 0 ; i<NbStationLign ; i++)
        {
            for (int j = 0 ; j<NbStationLign ; j++)
            {
                count++ ;
                StationDeBase stationDeBase = new StationDeBase(XStation ,YStation,count) ;
                XStation = XStation+tailleStation ;
                listeSB.add(stationDeBase);

            }
            XStation = tailleStation /2 ;
            YStation = YStation+tailleStation ;

        }
    }
    /*-------------------------------------------*/
    public void CreateNode()
    {
        for (int i = 0 ; i<nombreNoeud ; i++)
        {
            Random random = new Random( );
            int x = random.nextInt(tailleZone);
            int y = random.nextInt(tailleZone);
            Noeud noeud = new Noeud(i ,x ,y) ;
            listeNoeuds.add(noeud);
        }
    }

    /*---Positioner les Noeud dans 4 Station ---*/
    public void PositionerStationFour()
    {
        for (int i = 0 ; i<nombreNoeud ; i++)
        {

            Noeud noeud = listeNoeuds.elementAt(i);

            if (noeud.x<=tailleStation && noeud.y<tailleStation)
            {
                listeNoeuds.elementAt(i).sb = 1 ;
                listeSB.elementAt(2).listeNoeud.add(noeud);
            }
            if (noeud.x>tailleStation && noeud.y<=tailleStation)
            {
                listeNoeuds.elementAt(i).sb = 2;
                listeSB.elementAt(3).listeNoeud.add(noeud);
            }
            if (noeud.x<=tailleStation && noeud.y>=tailleStation)
            {
                listeNoeuds.elementAt(i).sb = 3  ;
                listeSB.elementAt(0).listeNoeud.add(noeud);
            }
            if (noeud.x>tailleStation && noeud.y>tailleStation)
            {
                listeNoeuds.elementAt(i).sb = 4 ;
                listeSB.elementAt(1).listeNoeud.add(noeud);
            }

        }
        //Pour la division de zone
        Line l1=new Line(tailleStation ,0 ,tailleStation ,tailleZone);
        Line l2=new Line(0,tailleStation,tailleZone,tailleStation);
        simulationPane.getChildren().addAll(l1,l2);
    }
    /*---Positioner les Noeud dans 9 Station ---*/
    public void PositionerStationNine()
    {
        int z = tailleZone / NbStationLign;
        for (int i = 0; i < nombreNoeud; i++)
        {
            Noeud noeud = listeNoeuds.elementAt(i);

            /*-----Les Noeud appartient aux Station 1------------*/
            if (noeud.x <= z && noeud.y <= z) {
                noeud.sb = 1;
                listeSB.elementAt(0).listeNoeud.add(noeud);
            }
            /*-----Les Noeud appartient aux Station 2------------*/
            if (noeud.x > z && noeud.x <= z + tailleStation && noeud.y <= z) {
                noeud.sb = 2;
                listeSB.elementAt(1).listeNoeud.add(noeud);
            }
            /*-----Les Noeud appartient aux Station 3------------*/
            if (noeud.x > z + tailleStation && noeud.y <= z) {
                noeud.sb = 3;
                listeSB.elementAt(2).listeNoeud.add(noeud);
            }
            /*-----Les Noeud appartient aux Station 4------------*/
            if (noeud.x <= z && noeud.y > z && noeud.y <= z + tailleStation) {
                noeud.sb = 4;
                listeSB.elementAt(3).listeNoeud.add(noeud);
            }
            /*-----Les Noeud appartient aux Station 5------------*/
            if (noeud.x > z && noeud.y > z && noeud.y <= z + tailleStation) {
                noeud.sb = 5;
                listeSB.elementAt(4).listeNoeud.add(noeud);
            }
            /*-----Les Noeud appartient aux Station 6------------*/
            if (noeud.x > z + tailleStation && noeud.y > z && noeud.y <= z + tailleStation) {
                noeud.sb = 6;
                listeSB.elementAt(5).listeNoeud.add(noeud);
            }
            /*-----Les Noeud appartient aux Station 7------------*/
            if (noeud.x <= z && noeud.y > z + tailleStation) {
                noeud.sb = 7;
                listeSB.elementAt(6).listeNoeud.add(noeud);
            }
            /*-----Les Noeud appartient aux Station 8------------*/
            if (noeud.x > z && noeud.y > z + tailleStation) {
                noeud.sb = 8;
                listeSB.elementAt(7).listeNoeud.add(noeud);
            }
            /*-----Les Noeud appartient aux Station 9------------*/
            if (noeud.x > z + tailleStation && noeud.y > z + tailleStation)
            {
                noeud.sb = 9;
                listeSB.elementAt(8).listeNoeud.add(noeud);
            }

            listeNoeuds.add(noeud);
        }
        Line l1=new Line(tailleStation ,0 ,tailleStation ,tailleZone);
        Line l2=new Line(z+tailleStation,0,z+tailleStation,tailleZone);
        Line l3=new Line(0,tailleStation,tailleZone,tailleStation);
        Line l4=new Line(0,z+tailleStation,tailleZone,z+tailleStation);

        simulationPane.getChildren().addAll(l1,l2,l3,l4);
    }

    /*---Positioner les Noeud dans 16 Station ---*/
    public void PositionnerStationSixteen()
    {

        int z = tailleZone / NbStationLign;
        for (int i = 0; i < nombreNoeud; i++)
        {
            Noeud noeud = listeNoeuds.elementAt(i);

            /*-----Les Noeud appartient aux Station 1------------*/
            if (noeud.x <= z && noeud.y <= z) {
                noeud.sb = 1;
                listeSB.elementAt(0).listeNoeud.add(noeud);
            }
            /*-----Les Noeud appartient aux Station 2------------*/
            if (noeud.x > z && noeud.x <= z + tailleStation && noeud.y <= z) {
                noeud.sb = 2;
                listeSB.elementAt(1).listeNoeud.add(noeud);
            }
            /*-----Les Noeud appartient aux Station 3------------*/
            if (noeud.x > z + tailleStation && noeud.x <= 2 * z + tailleStation && noeud.y <= z) {
                noeud.sb = 3;
                listeSB.elementAt(2).listeNoeud.add(noeud);
            }
            /*-----Les Noeud appartient aux Station 4------------*/
            if (noeud.x > 2 * z + tailleStation && noeud.y <= z) {
                noeud.sb = 4;
                listeSB.elementAt(3).listeNoeud.add(noeud);
            }
            /*-----Les Noeud appartient aux Station 5------------*/
            if (noeud.x <= z && noeud.y > z && noeud.y <= z + tailleStation) {
                noeud.sb = 5;
                listeSB.elementAt(4).listeNoeud.add(noeud);
            }
            /*-----Les Noeud appartient aux Station 6------------*/
            if (noeud.x > z && noeud.x <= z + tailleStation && noeud.y > z && noeud.y <= z + tailleStation) {
                noeud.sb = 6;
                listeSB.elementAt(5).listeNoeud.add(noeud);
            }
            /*-----Les Noeud appartient aux Station 7------------*/
            if (noeud.x > z + tailleStation && noeud.x <= 2 * z + tailleStation && noeud.y > z && noeud.y <= z + tailleStation) {
                noeud.sb = 7;
                listeSB.elementAt(6).listeNoeud.add(noeud);
            }
            /*-----Les Noeud appartient aux Station 8------------*/
            if (noeud.x > 2 * z + tailleStation && noeud.y > z && noeud.y <= z + tailleStation) {
                noeud.sb = 8;
                listeSB.elementAt(7).listeNoeud.add(noeud);
            }
            /*-----Les Noeud appartient aux Station 9------------*/
            if (noeud.x <= z && noeud.y > z + tailleStation && noeud.y <= 2 * z + tailleStation) {
                noeud.sb = 9;
                listeSB.elementAt(8).listeNoeud.add(noeud);
            }
            /*-----Les Noeud appartient aux Station 10------------*/
            if (noeud.x > z && noeud.x <= z + tailleStation && noeud.y <= 2 * z + tailleStation && noeud.y > z + tailleStation) {
                noeud.sb = 10;
                listeSB.elementAt(9).listeNoeud.add(noeud);
            }
            /*-----Les Noeud appartient aux Station 11------------*/
            if (noeud.x > z + tailleStation && noeud.x <= 2 * z + tailleStation && noeud.y <= 2 * z + tailleStation && noeud.y > z + tailleStation) {
                noeud.sb = 11;
                listeSB.elementAt(10).listeNoeud.add(noeud);
            }
            /*-----Les Noeud appartient aux Station 12------------*/
            if (noeud.x > 2 * z + tailleStation && noeud.y <= 2 * z + tailleStation && noeud.y > z + tailleStation) {
                noeud.sb = 12;
                listeSB.elementAt(11).listeNoeud.add(noeud);
            }
            /*-----Les Noeud appartient aux Station 13------------*/
            if (noeud.x <= z && noeud.y > 2 * z + tailleStation) {
                noeud.sb = 13;
                listeSB.elementAt(12).listeNoeud.add(noeud);
            }
            /*-----Les Noeud appartient aux Station 14------------*/
            if (noeud.x > z && noeud.x <= z + tailleStation && noeud.y > 2 * z + tailleStation) {
                noeud.sb = 14;
                listeSB.elementAt(13).listeNoeud.add(noeud);
            }
            /*-----Les Noeud appartient aux Station 15------------*/
            if (noeud.x > z + tailleStation && noeud.x <= 2 * z + tailleStation && noeud.y > 2 * z + tailleStation) {
                noeud.sb = 15;
                listeSB.elementAt(14).listeNoeud.add(noeud);
            }
            /*-----Les Noeud appartient aux Station 16 ------------*/
            if (noeud.x > 2 * z + tailleStation && noeud.y > 2 * z + tailleStation) {
                noeud.sb = 16;
                listeSB.elementAt(15).listeNoeud.add(noeud);
            }

            listeNoeuds.add(noeud);
        }


        Line l1 = new Line(0, tailleStation, tailleZone, tailleStation);
        Line l2 = new Line(0, z + tailleStation, tailleZone, z + tailleStation);
        Line l3 = new Line(0, 2 * z + tailleStation, tailleZone, 2 * z + tailleStation);


        Line l5 = new Line(tailleStation, 0, tailleStation, tailleZone);
        Line l6 = new Line(z + tailleStation, 0, z + tailleStation, tailleZone);
        Line l4 = new Line(2 * z + tailleStation, 0, 2 * z + tailleStation, tailleZone);

        simulationPane.getChildren().addAll(l1, l2, l3, l4, l5, l6);
    }
    /*-------------------------------------------*/
    public void DessinerStation()
    {
        for (int i = 0 ;i<nombreStation ;i++)
        {
            StationDeBase sb = listeSB.elementAt(i) ;
            int x = sb.x ;
            int y = sb.y ;
            ImageView imageView = new ImageView(sb.image) ;
            imageView.setX(x-40);
            imageView.setY(y-45);
            simulationPane.getChildren().add(imageView);
        }
        Line l1=new Line(tailleStation ,0 ,tailleStation ,tailleZone);
        Line l2=new Line(0,tailleStation,tailleZone,tailleStation);
        simulationPane.getChildren().addAll(l1,l2);
    }
    /*-------------------------------------------*/
    public void DessinerNoeud(){

        for (int i = 0 ;i<nombreNoeud ;i++)
        {
            Noeud noeud = listeNoeuds.elementAt(i) ;

            noeud.imageView.addEventHandler(MouseEvent.MOUSE_CLICKED,event ->
            {
                Noeud Node2 ;
                ImageView imageView = (ImageView) event.getSource();
                PathTransition pathTransition = new PathTransition() ;
                Circle circle = new Circle(imageView.getX()+10,imageView.getY()+25 ,4) ;
                pathTransition.setNode(imageView);
                pathTransition.setPath(circle);
                pathTransition.setDuration(Duration.seconds(1));
                pathTransition.play();
                System.out.println("X ="+imageView.getX() +" Y ="+imageView.getY()+" Is selected ="+isSeleted);
                if (!isSeleted)
                {
                    isSeleted = true  ;
                    Node1 = noeud ;
                    System.out.println("is selected"+isSeleted);
                    return;
                }
                if (isSeleted)
                {
                    isSeleted=false  ;
                    Node2 = noeud ;
                    System.out.println("is selected"+isSeleted);
                    if (Node1.id !=Node2.id)
                    {
                        nbAppel++ ;
                        toast = new Text("a call has been established") ;
                        toast.setStyle("-fx-fill: white");
                        snackbar.enqueue( new JFXSnackbar.SnackbarEvent(toast));
                        drawLine(Node1,Node2,Duration.seconds(1));
                    }
                    else {
                        isSeleted = true;
                    }
                    return;
                }
            });
            manager.setDepth(noeud.getImageView(),5);
            JFXRippler rippler =  new JFXRippler(noeud.getImageView()) ;
            rippler.setRipplerFill(Paint.valueOf("#ff0000"));
            rippler.setMaskType(JFXRippler.RipplerMask.CIRCLE);
            simulationPane.getChildren().addAll(noeud.getImageView(),rippler) ;
        }
    }

    /*-----Dessiner Les appels entre les Noeuds----*/
    public void drawLine(Noeud Node1 , Noeud Node2 , Duration duration)
    {

        Path path = new Path();

        StationDeBase stationDeBaseNode1 = null ,stationDeBaseNode2 = null;

        for (int i = 0; i < listeSB.size(); i++)
        {
            //Trouver la station de base de Noeud 1
            if (Node1.sb == listeSB.elementAt(i).id)
            {
                stationDeBaseNode1 = listeSB.elementAt(i);
            }

            //Trouver la station de base de Noeud 2
            if (Node2.sb == listeSB.elementAt(i).id)
            {
                stationDeBaseNode2 = listeSB.elementAt(i);

            }

        }

        //Tester Si les deux Noeude sont dans le meme Zone
        if (stationDeBaseNode1.id == stationDeBaseNode2.id)
        {
            path.getElements().addAll(
                    new MoveTo(Node1.x, Node1.y),
                    new LineTo(stationDeBaseNode1.x,stationDeBaseNode1.y),
                    new LineTo(Node2.x,Node2.y));


            PathTransition animation = createPathAnimation(path ,duration);
            animation.play();
            ImageView imageView = AppelCirculation(path);
            listAppel.add(new Call(Node1,Node2,listeLine,imageView)) ;
        }
        else
        {
            path.getElements().addAll(
                    new MoveTo(Node1.x, Node1.y),
                    new LineTo(stationDeBaseNode1.x, stationDeBaseNode1.y),
                    new LineTo(stationDeBaseNode2.x,stationDeBaseNode2.y),
                    new LineTo(Node2.x,Node2.y)) ;

            path.setFill(Color.color(Math.random(),Math.random(),Math.random()));


            PathTransition animation = createPathAnimation(path ,duration);
            animation.play();
            ImageView imageView =AppelCirculation(path);
                listAppel.add(new Call(Node1,Node2,listeLine,imageView)) ;
        }


    }

    /*--------------Random Call--------------------*/
    public void RandomCall()
    {

        if (!tailleZoneTextField.getText().isEmpty() &&
                !nombreNoeudTextField.getText().isEmpty() &&
                   !nombreStationComboBox.getSelectionModel().isEmpty())
        {
            Random random = new Random() ;

            Noeud noeud1 = listeNoeuds.elementAt(random.nextInt(nombreNoeud)) ;
            Noeud noeud2 = listeNoeuds.elementAt(random.nextInt(nombreNoeud)) ;

            toast = new Text("a call has been established") ;
            toast.setStyle("-fx-fill: white");
            snackbar.enqueue( new JFXSnackbar.SnackbarEvent(toast));

            drawLine(noeud1,noeud2,Duration.seconds(2));
        }
    }

    /*---Animer la  Création des appel entre les noeud-(This code is from stackoverflow)----*/
    public PathTransition createPathAnimation(Path path, Duration duration)
    {
        listeLine.clear();
        // move a node along a path. we want its position
        Circle pen = new Circle(0, 0, 4);

        // create path transition
        PathTransition pathTransition = new PathTransition(duration, path, pen);
        pathTransition.currentTimeProperty().addListener(new ChangeListener<Duration>() {

            Location oldLocation = null;

            /**
             * Draw a line from the old location to the new location
             */
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {

                // skip starting at 0/0
                if (oldValue == Duration.ZERO)
                    return;

                // get current location
                double x = pen.getTranslateX();
                double y = pen.getTranslateY();

                // initialize the location
                if (oldLocation == null) {
                    oldLocation = new Location();
                    oldLocation.x = x;
                    oldLocation.y = y;
                    return;
                }
                Line l = new Line(oldLocation.x, oldLocation.y, x, y) ;

                listeLine.addElement(l);

                l.setStroke(Color.color(Math.random(),Math.random(),Math.random()));

                simulationPane.getChildren().addAll(l);

                // update old location with current one
                oldLocation.x = x;
                oldLocation.y = y;
            }
        });

        return pathTransition;
    }
    public static class Location
    {
        double x;
        double y;
    }
    /*---------------------------Circulation des appels------------------------*/
    public  ImageView AppelCirculation (Path path)
    {
        Image image = new Image("/ressources/RandomAppel.png", 10, 10, false, false);
        PathTransition pathTransition = new PathTransition() ;
        pathTransition.setDuration(Duration.seconds((10-(mobilitySpeed.getValue())+2)));
        ImageView imageView1 = new ImageView(image) ;
        pathTransition.setNode(imageView1);
        pathTransition.setPath(path);
        pathTransition.setCycleCount(PathTransition.INDEFINITE);
        simulationPane.getChildren().add(imageView1 );
        pathTransition.play();
        return imageView1 ;
    }
    /*--------------Enrigestrier les Positiones dse noeud et station -------------*/
    public void Save ()
    {

        toast = new Text("The file has been saved") ;
        toast.setStyle("-fx-fill: white");
        snackbar.enqueue( new JFXSnackbar.SnackbarEvent(toast));
        if (!file.exists())
        {
            try
            {
                file.createNewFile() ;
                FileWriter fileReader = new FileWriter(file) ;
                BufferedWriter bufferedWriter = new BufferedWriter(fileReader) ;
                for (int i = 0 ; i<nombreStation ; i++)
                {
                    bufferedWriter.write("Station de Base "
                            +listeSB.elementAt(i).id
                            +" X="+listeSB.elementAt(i).x
                            +" Y = "+listeSB.elementAt(i).y +"\n");
                    bufferedWriter.flush();
                }
                for (int j = 0 ; j<nombreNoeud;j++)
                {
                    bufferedWriter.write("Noeud "
                            + listeNoeuds.elementAt(j).id
                            +" x= "+listeNoeuds.elementAt(j).x
                            +" y="+listeNoeuds.elementAt(j).y+ " SB ="
                            +listeNoeuds.elementAt(j).sb+"\n");
                    bufferedWriter.flush();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    /*--------------------Sortir de Programme-------------*/
    public void  Exit()
    {
        Platform.exit();
    }

    /*------------Effacer l'écran de simulation-----------*/
    public void Clear()
    {

        tailleZoneTextField.clear();
        nombreStationComboBox.getSelectionModel().clearSelection();
        nombreNoeudTextField.clear();
        simulationPane.getChildren().clear();
        listeNoeuds.clear();
        listeSB.clear();
        listAppel.clear();
        listeLine.clear();
        stopWork = false ;
        nbAppel = 0 ;
        simulationPane.getChildren().add(hidePane) ;
        hidePane.setVisible(true);
        toast = new Text("The pane has been cleared ") ;
        toast.setStyle("-fx-fill: white");
        snackbar.enqueue( new JFXSnackbar.SnackbarEvent(toast));
    }
    /*-----------Mobilitiy-------------------*/
    public void changePositionNode ()
    {

        int speed = (int) mobilitySpeed.getValue();
        for (int i =0 ; i<listeNoeuds.size();i++)
        {
            Random random = new Random();

            int newX = random.nextInt(tailleZone);
            int newY = random.nextInt(tailleZone);

            Noeud noeud = listeNoeuds.elementAt(i);


            TranslateTransition transition =  new TranslateTransition() ;

            transition.setDuration(Duration.seconds(10-speed));
            transition.setNode(noeud.getImageView());

            transition.setFromX(noeud.getImageView().getTranslateX());
            transition.setFromY(noeud.getImageView().getTranslateY());

            transition.setToX((newX-noeud.getImageView().getX())-10);
            transition.setToY((newY-noeud.getImageView().getY())-25);


            noeud.x = newX ;
            noeud.y = newY ;

            transition.play();


            for (int k = 0; k < nbAppel; k++) {
                System.out.println("nbAppel"+nbAppel);

                if (noeud.id == listAppel.elementAt(k).getDistination().id) {
                    simulationPane.getChildren().remove(listAppel.elementAt(k).getImageView());
                    Noeud r = listAppel.elementAt(k).getReciption();

                    for (int l = 0; l < listAppel.elementAt(k).getLiseLine().size(); l++)
                    {
                        simulationPane.getChildren().remove(listAppel.elementAt(k).getLiseLine().elementAt(l));

                    }
                    listAppel.removeElementAt(k);
                    PositionerStationFour();
                    drawLine(noeud, r,Duration.seconds((10-speed)+1));
                    return;
                }
                if (noeud.id == listAppel.elementAt(k).getReciption().id) {
                    simulationPane.getChildren().remove(listAppel.elementAt(k).getImageView());
                    Noeud d = listAppel.elementAt(k).getDistination();
                    for (int l = 0; l < listAppel.elementAt(k).getLiseLine().size(); l++) {
                        simulationPane.getChildren().remove(listAppel.elementAt(k).getLiseLine().elementAt(l));

                    }
                    listAppel.removeElementAt(k);
                    PositionerStationFour();
                    drawLine(d, noeud,Duration.seconds((10-speed)+1));
                    return;
                }
            }
       }

    }

    public  void  mobile()
    {
        stopWork = false ;
        new Mobile().start();
    }

    public void pause()
    {
        stopWork = true ;
        toast = new Text("Mobility mode disabled") ;
        toast.setStyle("-fx-fill: white");
        snackbar.enqueue( new JFXSnackbar.SnackbarEvent(toast));
        }



    public void initialize(URL location, ResourceBundle resources)
    {


        nombreStationComboBox.getItems().addAll("4","9","16");
        manager= new JFXDepthManager() ;
        manager.setDepth(mobilitySpeed,5);
        manager.setDepth(node,5);
        manager.setDepth(sb, 5);
        mobilitySpeed.addEventHandler(MouseEvent.MOUSE_PRESSED, event ->
        {
            toast = new Text("Mobility speed changed to "+(int)mobilitySpeed.getValue()) ;
            toast.setStyle("-fx-fill: white");
            snackbar.enqueue( new JFXSnackbar.SnackbarEvent(toast));
        });

    }

    class Mobile extends Thread
    {
        @Override
        public void run()
        {
            try {
                switch ((int) mobilitySpeed.getValue())
                {
                    case 1:
                        Thread.sleep(12000); break;
                    case 2:
                        Thread.sleep(11000); break;
                    case 3:
                        Thread.sleep(10000); break;
                    case 4:
                        Thread.sleep(9000); break;
                    case 5:
                        Thread.sleep(8000); break;
                    case 6:
                        Thread.sleep(7000); break;
                    case 7:
                        Thread.sleep(6000); break;
                    case 8:
                        Thread.sleep(5000); break;
                    case 9:
                        Thread.sleep(4000); break;
                }

            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (!stopWork)
                    {

                        changePositionNode();
                        mobile();
                    }


                }
            });

        }
    }
}
