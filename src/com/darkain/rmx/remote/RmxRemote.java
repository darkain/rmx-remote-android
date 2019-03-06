package com.darkain.rmx.remote;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class RmxRemote extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //super.setTitle("RMX Remote");
        
        
        final ImageButton play = (ImageButton) findViewById(R.id.btnPlay);
        play.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { sendMessage("play"); }
        });
        
        final ImageButton pause = (ImageButton) findViewById(R.id.btnPause);
        pause.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { sendMessage("pause"); }
        });
        
        final ImageButton stop = (ImageButton) findViewById(R.id.btnStop);
        stop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { sendMessage("stop"); }
        });
        
        final ImageButton volup = (ImageButton) findViewById(R.id.btnVolUp);
        volup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { sendMessage("up"); }
        });
        
        final ImageButton voldown = (ImageButton) findViewById(R.id.btnVolDown);
        voldown.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { sendMessage("down"); }
        });
        
        final ImageButton mute = (ImageButton) findViewById(R.id.btnMute);
        mute.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { sendMessage("mute"); }
        });
        
        final ImageButton rmx = (ImageButton) findViewById(R.id.btnRmx);
        rmx.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { sendMessage("rmx"); }
        });
        
        final ImageButton prevtrack = (ImageButton) findViewById(R.id.btnPrevTrack);
        prevtrack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { sendMessage("prev"); }
        });
        
        final ImageButton nexttrack = (ImageButton) findViewById(R.id.btnNextTrack);
        nexttrack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { sendMessage("next"); }
        });
        
    }
    
    
    private void sendMessage(String text) {
    	broadcastPacket(text, 31055);
     }
    
    
    
    InetAddress getBroadcastAddress() throws IOException {
        WifiManager wifi = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        if (wifi == null) throw new IOException();
        
        DhcpInfo dhcp = wifi.getDhcpInfo();
        if (dhcp == null) throw new IOException();

        int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
        byte[] quads = new byte[4];
        for (int k = 0; k < 4; k++)
          quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
        return InetAddress.getByAddress(quads);
    }
    
    
    void broadcastPacket(String data, int port) {
    	try {
	    	DatagramSocket socket = new DatagramSocket();
	    	socket.setBroadcast(true);
	    	DatagramPacket packet = new DatagramPacket(data.getBytes(), data.length(),
	    	    getBroadcastAddress(), port);
	    	socket.send(packet);
	    	//showAlert("Sent: " + data);
    	} catch (Throwable E) {
    		showAlert(E.getMessage());
    	}
    }
    
    
    void showAlert(String text) {
    	AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
    	alt_bld.setMessage(text)
    		.setCancelable(false)
    		.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int id) {
    				dialog.cancel();
    			}
    		});
    	
    	AlertDialog alert = alt_bld.create();
    	alert.setTitle("Notice");
    	alert.setIcon(R.drawable.icon);
    	alert.show();
    	
    }
}