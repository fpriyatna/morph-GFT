package es.upm.fi.dia.oeg.obdi.wrapper.r2rml.fusiontables.engine

import scala.collection.JavaConversions._
import com.google.api.services.fusiontables.model.Sqlresponse
import es.upm.fi.dia.oeg.obdi.core.engine.AbstractResultSet

class FusionTablesResultSet(val rows : List[java.util.List[java.lang.Object]]) 
extends AbstractResultSet {
	val rowsIterator = rows.iterator;
	var activeRow : java.util.List[java.lang.Object] = Nil;
  
  
	def next() : Boolean = {
		var result:Boolean = false;
		
		if(this.rowsIterator.hasNext) {
			this.activeRow = rowsIterator.next();
			result = true;
		} else {
			result = false;
		}
		result;
	}

	def getString(columnIndex : Int) : String = {
		val result = this.activeRow.get(columnIndex).toString();
		result;
	}
	
	def getString(columnLabel : String ) : String = {
		val columnIndex = super.getColumnNames().indexOf(columnLabel);
		val result = this.getString(columnIndex);
		result;
	}
	
	def getInt(columnIndex : Int) : Integer = {
		val result = this.activeRow.get(columnIndex).asInstanceOf[Integer];
		result;
	}
	
	def getInt(columnLabel : String) : Integer = {
	  val result =  {
		  try {
		    val columnIndex = super.getColumnNames().indexOf(columnLabel);
		    this.getInt(columnIndex);
		  } catch {
		    case e:Exception => {
		    	null;
		    }
		  }	    
	  }
	  result;	  
	}	
	
}