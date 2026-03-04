package com.xdustatom.auryxmaps

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import com.xdustatom.auryxmaps.net.NetworkInfo
import com.xdustatom.auryxmaps.storage.CsvStore

class MainActivity:AppCompatActivity(){

lateinit var map:MapView
var loc:MyLocationNewOverlay?=null

override fun onCreate(savedInstanceState:Bundle?){
super.onCreate(savedInstanceState)

Configuration.getInstance().userAgentValue=packageName

setContentView(R.layout.activity_main)

map=findViewById(R.id.map)

map.setTileSource(TileSourceFactory.MAPNIK)

map.setMultiTouchControls(true)

map.controller.setZoom(13.0)

map.controller.setCenter(GeoPoint(45.46,9.19))

findViewById<FloatingActionButton>(R.id.recordBtn).setOnClickListener{

record()

}

checkPerm()

}

fun record(){

val p=loc?.myLocation ?: return

val net=NetworkInfo.snapshot(this)

CsvStore.append(
p.latitude,
p.longitude,
net.networkType,
net.dbm,
net.cellId
)

val marker=Marker(map)

marker.position=GeoPoint(p.latitude,p.longitude)

marker.setAnchor(Marker.ANCHOR_CENTER,Marker.ANCHOR_BOTTOM)

val color=when{

net.dbm==null -> Color.GRAY
net.dbm>-85 -> Color.GREEN
net.dbm>-100 -> Color.YELLOW
else -> Color.RED

}

marker.icon=resources.getDrawable(org.osmdroid.library.R.drawable.marker_default,null)

marker.icon.setTint(color)

marker.title="${net.networkType} ${net.dbm} dBm"

map.overlays.add(marker)

map.invalidate()

Toast.makeText(this,"Point saved",Toast.LENGTH_SHORT).show()

}

fun checkPerm(){

if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){

ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1)

return
}

enable()

}

fun enable(){

loc=MyLocationNewOverlay(GpsMyLocationProvider(this),map)

loc!!.enableMyLocation()

map.overlays.add(loc)

}

}
