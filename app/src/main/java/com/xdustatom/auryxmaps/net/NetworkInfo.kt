package com.xdustatom.auryxmaps.net

import android.content.Context
import android.telephony.*

data class NetworkSnapshot(
val networkType:String,
val dbm:Int?,
val cellId:String?
)

object NetworkInfo{

fun snapshot(ctx:Context):NetworkSnapshot{

val tm=ctx.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

var type="UNKNOWN"
var dbm:Int?=null
var cell:String?=null

try{

val cells=tm.allCellInfo

if(cells!=null){

for(c in cells){

if(c is CellInfoLte){

type="4G"

dbm=c.cellSignalStrength.dbm

cell=c.cellIdentity.ci.toString()

break
}

if(c is CellInfoNr){

type="5G"

dbm=c.cellSignalStrength.dbm

cell=c.cellIdentity.nci.toString()

break
}

if(c is CellInfoWcdma){

type="3G"

dbm=c.cellSignalStrength.dbm

cell=c.cellIdentity.cid.toString()

break
}

if(c is CellInfoGsm){

type="2G"

dbm=c.cellSignalStrength.dbm

cell=c.cellIdentity.cid.toString()

break
}

}

}

}catch(e:Exception){}

return NetworkSnapshot(type,dbm,cell)

}

}
