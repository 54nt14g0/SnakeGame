package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import auxiliaresGraficos.GraphicMap;
import datos.MejoresPuntuaciones;
import enums.Direccion;
import exceptions.GameOverException;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import model.Map;
import model.Snake;
import utilities.Singleton;

public class JuegoController implements Initializable {
	
	Singleton singleton = Singleton.getInstance();
	MejoresPuntuaciones mejoresPuntuaciones = singleton.getMejoresPuntuaciones();
	
	Map map = new Map(new Snake());
	
	GraphicMap gm;
	Timeline timeline;
	Direccion direccionAux;
	
	MenuController menuController;
	CierreSeguroController cierreSeguroController;

	@FXML
	private Canvas mapaJuego;

	@FXML
	private Label LblPuntos;
	
	@FXML
	private Label lblPlayer;
	
	@FXML
	private Button startGame;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
		
		
		lblPlayer.setText(singleton.getOpciones().getUsuarioSeleccionado().getName());
		gm = new GraphicMap(mapaJuego, map);

		gm.restartGraphicMap();
		
		timeline = new Timeline(new KeyFrame(Duration.millis(singleton.getOpciones().getVelocidad()), e -> {

			map.setearDireccionCulebra(direccionAux);

			try {
				map.moverSnake();
				actualizarPuntuacion ();
				gm.restartGraphicMap();
				
				
			} catch (GameOverException e1) {
				
				finalizarJuego();
				timeline.stop();
				guardarPuntos();
			}

		}));
		timeline.setCycleCount(Animation.INDEFINITE);
		
	}

	@FXML
	void empezarJuego(ActionEvent event) {

		setearEventoTeclado(event);
		startGame.setDisable(true);

		timeline.play();
		
	}

	private void finalizarJuego() {
		
		
		
		
		JOptionPane.showMessageDialog(null, "GAME OVER");
		

	}

	private void guardarPuntos() {
		Integer puntuacionFinal = map.getCantidadPuntos();
		String puntuacionFinalAux = puntuacionFinal.toString();
		mejoresPuntuaciones.setTextoEscritura(puntuacionFinalAux);
		mejoresPuntuaciones.guardarPuntuaciones(singleton.getOpciones().getUsuarioSeleccionado().getName());
		
	}

	private void setearEventoTeclado(ActionEvent event) {
		
		((Node) event.getSource()).getScene().setOnKeyReleased(new EventHandler<KeyEvent>() {
			
			@Override
			public void handle(KeyEvent event) {

				String tecla = event.getText();
				
				switch (tecla) {
				case "w":
					direccionAux = Direccion.ARRIBA;
					break;

				case "a":
					direccionAux = Direccion.IZQUIERDA;
					break;

				case "s":
					direccionAux = Direccion.ABAJO;
					break;

				case "d":
					direccionAux = Direccion.DERECHA;
					break;

				}
				
			}
		});

	}

	public Timeline getTimeline() {
		return this.timeline;
	}
	
	public void setMap(Map map){
		this.map = map;
		
		gm.setMap(map);
		gm.restartGraphicMap();
		
	}

	public Map getMap() {
		return map;
	}
	
	public void actualizarPuntuacion (){
		
			
			Integer puntosActuales = map.getCantidadPuntos();
			String puntosAux = puntosActuales.toString();
			LblPuntos.setText(puntosAux);
		
	}

}
