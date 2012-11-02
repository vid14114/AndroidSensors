/**
 * 
 */
package com.example.androidsensors;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import org.xmlpull.v1.XmlSerializer;

import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.text.method.MovementMethod;
import android.util.Xml;

/**
 * @author abideen
 *
 */
public class Coordinator implements Runnable, OnInitListener{	
	TextToSpeech tts;
	AndroidSensors and;
	ArrayList<String> inputOptions;
	ArrayList<String> outputOptions;
	Handler h;
/*
 * Take the output option first, calculate all the input values for it	
 */
	
	public Coordinator(ArrayList<String> inputOptions, ArrayList<String> outputOptions, AndroidSensors and, Handler h){
		this.and = and;
		this.inputOptions = inputOptions;
		this.outputOptions = outputOptions;
		this.h = h;
	}
	
	/**
	 * Option is XML
	 * @param inputOptions the input types the user selected
	 */
	public void generateXML(ArrayList<String> inputOptions){
		if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			and.displayError("Could not write to the external storage, an unknown error occured");
			return;
		}
		//create a new file called "new.xml" in the SD card, Here we start the generating the file	    
			XmlSerializer serializer = null;
			FileOutputStream fo = null;
			File generate = new File(Environment.getExternalStorageDirectory()+"/generated.xml");
	        try{
	        	generate.createNewFile();
	        	fo = new FileOutputStream(generate); //We need to bind the new file to a FileOutputStream
	        	serializer = Xml.newSerializer(); // We create a XMLSerializer in order to write xml to the file
	        	serializer.setOutput(fo, "UTF-8"); //Outputstream as output for the serializer, UTF-8 as encoding
	        	serializer.startDocument(null, true);
	        	serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
	        	serializer.startTag(null, "Results");
	        }
	        catch(IOException e){
	        	and.displayError("Couldn't create xml file");
	        	return;
	        }
		// The options the user chose are added from this point on
	    try{
	    	//////////////////////////////////////////////////////////////////////////////////////////////////////
	    	//Beginning of generating XML file
		    for(String option : inputOptions){
		    	if(option.equals("Movement")){
		    		serializer.startTag(null, "Speed in x,y,z");
		    		serializer.attribute(null, "MeasuredIn", "m/s²");
		    		serializer.startTag(null, "X direction");
		    		serializer.text(SensorListener.movementDirection[0]+"");
		    		serializer.endTag(null, "X direction");
		    		serializer.startTag(null, "Y direction");
		    		serializer.text(SensorListener.movementDirection[1]+"");
		    		serializer.endTag(null, "Y direction");
		    		serializer.startTag(null, "Z direction");
		    		serializer.text(SensorListener.movementDirection[2]+"");
		    		serializer.endTag(null, "Z direction");
		    		serializer.endTag(null, "Speed in x,y,z");
				}
				if(option.equals("Accelerometer")){
		    		serializer.startTag(null, "Accelerometer");
		    		serializer.attribute(null, "MeasuredIn", "m/s²");
		    		serializer.startTag(null, "X coordinate");
		    		serializer.text(SensorListener.accelerometer[0]+"");
		    		serializer.endTag(null, "X coordinate");
		    		serializer.startTag(null, "Y coordinate");
		    		serializer.text(SensorListener.accelerometer[1]+"");
		    		serializer.endTag(null, "Y coordinate");
		    		serializer.startTag(null, "Z coordinate");
		    		serializer.text(SensorListener.accelerometer[2]+"");
		    		serializer.endTag(null, "Z coordinate");
		    		serializer.endTag(null, "Accelerometer");
				}
				if(option.equals("Magnet")){
					serializer.startTag(null, "Magnetic Field");
					serializer.attribute(null, "MeasuredIn", "micro-Tesla");
		    		serializer.startTag(null, "X");
		    		serializer.text(SensorListener.magnet[0]+"");
		    		serializer.endTag(null, "X");
		    		serializer.startTag(null, "Y");
		    		serializer.text(SensorListener.magnet[1]+"");
		    		serializer.endTag(null, "Y");
		    		serializer.startTag(null, "Z");
		    		serializer.text(SensorListener.magnet[2]+"");
		    		serializer.endTag(null, "Z");
		    		serializer.endTag(null, "Magnetic Field");
				}
				if(option.equals("Direction")){
					serializer.startTag(null, "Direction");
					serializer.attribute(null, "MeasuredIn", "m/s²");
		    		serializer.startTag(null, "X");
		    		serializer.text(SensorListener.movementDirection[0]+"");
		    		serializer.endTag(null, "X");
		    		serializer.startTag(null, "Y");
		    		serializer.text(SensorListener.movementDirection[1]+"");
		    		serializer.endTag(null, "Y");
		    		serializer.startTag(null, "Z");
		    		serializer.text(SensorListener.movementDirection[2]+"");
		    		serializer.endTag(null, "Z");
		    		serializer.endTag(null, "Direction");
				}
				if(option.equals("Air Pressure")){
					serializer.startTag(null, "Pressure");
					serializer.attribute(null, "MeasuredIn", "millibar");
		    		serializer.text(SensorListener.pressure+"");
		    		serializer.endTag(null, "Pressure");
				}
				if(option.equals("Temperature")){
					serializer.startTag(null, "Temperature");
		    		serializer.attribute(null, "MeasuredIn", "Celsius");
		    		serializer.text(SensorListener.temperature+"");
		    		serializer.endTag(null, "Temperature");
				}
				if(option.equals("Light")){
					serializer.startTag(null, "Light");
					serializer.attribute(null, "MeasuredIn", "SI lux");
		    		serializer.text(SensorListener.light+"");
		    		serializer.endTag(null, "Light");
				}
				if(option.equals("Microphone") && and.speak(true)){
					while(and.speechResults.isEmpty());
					serializer.startTag(null, "Microphone");
					for(String text:and.speechResults){
						serializer.startTag(null, "RecognisedText");
						serializer.text(text);
						serializer.endTag(null, "RecognisedText");
					}					
					serializer.endTag(null, "Microphone");					
				}
				if(option.equals("Camera")){
				}
				if(option.equals("GPS")){
					serializer.startTag(null, "Location");
					serializer.attribute(null, "ProvidedBy", AndroidLocationListener.locationInfo.getProvider());
					serializer.startTag(null, "Longitude");
					serializer.text(AndroidLocationListener.locationInfo.getLongitude()+"");
					serializer.endTag(null, "Longitude");
					serializer.startTag(null, "Latitude");
					serializer.text(AndroidLocationListener.locationInfo.getLatitude()+"");
					serializer.endTag(null, "Latitude");
					serializer.endTag(null, "Location");
				}
		    }	
		////////////////////////////////////////////// End of generating, end tags to be added now
		    serializer.endTag(null, "Results");
		    serializer.endDocument();
		    serializer.flush();
		    fo.close();
		    and.displayError("XML file generated: "+Environment.getExternalStorageDirectory()+"/generated.xml");
	    }catch(IOException e){
	    	and.displayError("An Unknown error occured");
	    }
	}

	/**
	 * Generate a wav file where tts spoken words will be recorded into
	 * @param inputOptions
	 */
	public void generateAudio(ArrayList<String> inputOptions){	
		if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			and.displayError("Could not write to the external storage, an unknown error occured");
			return;
		}
		//Initialize TTS
		tts = new TextToSpeech(and, new OnInitListener() {			
			public void onInit(int status) {
				if(status == TextToSpeech.ERROR){
					and.displayError("Text to Speech not working! Exiting");
					System.exit(0);
				}
				tts.setLanguage(Locale.ENGLISH);
			}
		});
		String text = ""; //This is the text to be recorded to the wav file
		//////////////////////////////////////////////////////////////////////////////////////////////////////
		for(String option : inputOptions){
			if(option.equals("Movement"))
				text+="Measuring movement on x,y and z axis in meter per seconds to the power of 2."+
			"Axis x is "+SensorListener.movementDirection[0]+" meter per seconds to the power of 2, "+
			"Axis y is "+SensorListener.movementDirection[1]+" meter per seconds to the power of 2, "+
			"Axis z is "+SensorListener.movementDirection[2]+" meter per seconds to the power of 2. The End ";		    		
			if(option.equals("Accelerometer"))
				text+="Measuring the accelerometer on x,y and z axis in meter per seconds to the power of 2."+
				    	"Axis x is "+SensorListener.accelerometer[0]+" meter per seconds to the power of 2, "+
				    	"Axis y is "+SensorListener.accelerometer[1]+" meter per seconds to the power of 2, "+
				    	"Axis z is "+SensorListener.accelerometer[2]+" meter per seconds to the power of 2. The End ";
			if(option.equals("Magnet"))
				text+="Measuring the magnetic filed which is measured in micro Tesla on the x, y and z axis"+
				    	"X value is "+SensorListener.magnet[0]+" micro Tesla, "+
				    	"Y value is "+SensorListener.magnet[1]+" micro Tesla, "+
				    	"Z value is "+SensorListener.magnet[2]+" micro Tesla. The End ";					
			if(option.equals("Direction"))
				text+="Measuring the direction on x,y and z axis in meter per seconds to the power of 2."+
				    	"Axis x is "+SensorListener.movementDirection[0]+" meter per seconds to the power of 2, "+
				    	"Axis y is "+SensorListener.movementDirection[1]+" meter per seconds to the power of 2, "+
				    	"Axis z is "+SensorListener.movementDirection[2]+" meter per seconds to the power of 2. The End ";		 				
			if(option.equals("Air Pressure"))
				text+="Measuring the air pressure in the room in millibar."+
				    	"The air pressure in the room is "+SensorListener.pressure+" millibar. The End ";		 					
			if(option.equals("Temperature"))
				text+="Measuring the temperature in Celsius."+
				    	"The temperature is "+SensorListener.temperature+" degrees Celsius. The End ";					
			if(option.equals("Light"))
				text+="Measuring light in SI lux."+
				    	"Light is "+SensorListener.light+" SI lux. The End ";				
			if(option.equals("Microphone") && and.speak(true)){
				text+="The user recorded something, this following was understood: "; 
				for(String temp : and.speechResults)
					text+=temp+" ";
				text+="The End ";
			}
			if(option.equals("Camera")){
			}
			if(option.equals("GPS"))
				text+="Trying to locate the user."+
				    	"The Location was provided by "+AndroidLocationListener.locationInfo.getProvider()+", "+
				    	"The Longitudinal value is "+AndroidLocationListener.locationInfo.getLongitude()+", "+
				    	"The Latitudinal value is "+AndroidLocationListener.locationInfo.getLatitude()+". The End ";	
		}	
///////////////////////////////////////////// Now writing the recorded data into a file
		String destFileName = Environment.getExternalStorageDirectory().getAbsolutePath()+"/generated.wav";
		tts.synthesizeToFile(text, null, destFileName);
	}
	
	/**
	 * Now the text to speech is directly outputed to the user
	 * @param inputOptions
	 */
	public void ttsSpeak(ArrayList<String> inputOptions){	
		//Initialize TTS
		tts = new TextToSpeech(and, new OnInitListener() {			
			public void onInit(int status) {
				if(status == TextToSpeech.ERROR){
					and.displayError("Text to Speech not working! Exiting");
					System.exit(0);
				}
				tts.setLanguage(Locale.ENGLISH);
			}
		});
		//////////////////////////////////////////////////////////////////////////////////////////////////////
		for(String option : inputOptions){
			if(option.equals("Movement"))
				tts.speak("Measuring movement on x,y and z axis in meter per seconds to the power of 2."+
			"Axis x is "+SensorListener.movementDirection[0]+" meter per seconds to the power of 2, "+
			"Axis y is "+SensorListener.movementDirection[1]+" meter per seconds to the power of 2, "+
			"Axis z is "+SensorListener.movementDirection[2]+" meter per seconds to the power of 2. The End ",TextToSpeech.QUEUE_ADD,null);		    		
			if(option.equals("Accelerometer"))
				tts.speak("Measuring the accelerometer on x,y and z axis in meter per seconds to the power of 2."+
				    	"Axis x is "+SensorListener.accelerometer[0]+" meter per seconds to the power of 2, "+
				    	"Axis y is "+SensorListener.accelerometer[1]+" meter per seconds to the power of 2, "+
				    	"Axis z is "+SensorListener.accelerometer[2]+" meter per seconds to the power of 2. The End ",TextToSpeech.QUEUE_ADD,null);
			if(option.equals("Magnet"))
				tts.speak("Measuring the magnetic filed which is measured in micro Tesla on the x, y and z axis"+
				    	"X value is "+SensorListener.magnet[0]+" micro Tesla, "+
				    	"Y value is "+SensorListener.magnet[1]+" micro Tesla, "+
				    	"Z value is "+SensorListener.magnet[2]+" micro Tesla. The End ", TextToSpeech.QUEUE_ADD, null);					
			if(option.equals("Direction"))
				tts.speak("Measuring the direction on x,y and z axis in meter per seconds to the power of 2."+
				    	"Axis x is "+SensorListener.movementDirection[0]+" meter per seconds to the power of 2, "+
				    	"Axis y is "+SensorListener.movementDirection[1]+" meter per seconds to the power of 2, "+
				    	"Axis z is "+SensorListener.movementDirection[2]+" meter per seconds to the power of 2. The End ",TextToSpeech.QUEUE_ADD,null);		 				
			if(option.equals("Air Pressure"))
				tts.speak("Measuring the air pressure in the room in millibar."+
				    	"The air pressure in the room is "+SensorListener.pressure+" millibar. The End ",TextToSpeech.QUEUE_ADD,null);		 					
			if(option.equals("Temperature"))
				tts.speak("Measuring the temperature in Celsius."+
				    	"The temperature is "+SensorListener.temperature+" degrees Celsius. The End ",TextToSpeech.QUEUE_ADD,null);					
			if(option.equals("Light"))
				tts.speak("Measuring light in SI lux."+
				    	"Light is "+SensorListener.light+" SI lux. The End ",TextToSpeech.QUEUE_ADD,null);				
			if(option.equals("Microphone") && and.speak(true)){
				tts.speak("The user recorded something, this following was understood: ", TextToSpeech.QUEUE_ADD, null); 
				for(String temp : and.speechResults)
					tts.speak(temp+" ", TextToSpeech.QUEUE_ADD, null);
				tts.speak("The End ",TextToSpeech.QUEUE_ADD,null);
			}
			if(option.equals("Camera")){
			}
			if(option.equals("GPS"))
				tts.speak("Trying to locate the user."+
				    	"The Location was provided by "+AndroidLocationListener.locationInfo.getProvider()+", "+
				    	"The Longitudinal value is "+AndroidLocationListener.locationInfo.getLongitude()+", "+
				    	"The Latitudinal value is "+AndroidLocationListener.locationInfo.getLatitude()+". The End ",TextToSpeech.QUEUE_ADD,null);	
		}
	}
	
	public void run() {
		for(String option: outputOptions){
			if(option.equals("XML"))
				generateXML(inputOptions);
			if(option.equals("View"))
				;
			if(option.equals("Dialog"))
				;
			if(option.equals("Audiofile"))
				generateAudio(inputOptions);
			if(option.equals("TTS"))
				ttsSpeak(inputOptions);
			if(option.equals("Email"))
				;
			if(option.equals("Phone Call"))
				;
			if(option.equals("SMS"))
				;
			if(option.equals("File"))
				;
			if(option.equals("Internet"))
				;
		}
	}

	public void onInit(int status) {
		
	}
	
}
