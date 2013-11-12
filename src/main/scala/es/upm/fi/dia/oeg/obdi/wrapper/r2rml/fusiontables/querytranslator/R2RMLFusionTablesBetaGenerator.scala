package es.upm.fi.dia.oeg.obdi.wrapper.r2rml.fusiontables.querytranslator

import es.upm.fi.dia.oeg.obdi.core.querytranslator.AbstractQueryTranslator
import org.apache.log4j.Logger
import scala.collection.JavaConversions._
import es.upm.fi.dia.oeg.obdi.core.querytranslator.AlphaResult
import es.upm.fi.dia.oeg.obdi.core.model.AbstractConceptMapping
import es.upm.fi.dia.oeg.obdi.core.querytranslator.AbstractBetaGenerator
import Zql.ZSelectItem
import es.upm.fi.dia.oeg.obdi.wrapper.r2rml.rdb.model.R2RMLTriplesMap
import es.upm.fi.dia.oeg.upm.morph.sql.MorphSQLSelectItem
import com.hp.hpl.jena.graph.Triple
import es.upm.fi.dia.oeg.obdi.core.model.AbstractPropertyMapping
import es.upm.fi.dia.oeg.obdi.wrapper.r2rml.rdb.model.R2RMLPredicateObjectMap
import es.upm.fi.dia.oeg.morph.base.Constants
import es.upm.fi.dia.oeg.obdi.wrapper.r2rml.rdb.model.R2RMLTermMap.TermMapType
import Zql.ZConstant
import es.upm.fi.dia.oeg.obdi.wrapper.r2rml.rdb.querytranslator.R2RMLQueryTranslator


class R2RMLFusionTablesBetaGenerator(pOwner : AbstractQueryTranslator) 
extends AbstractBetaGenerator(pOwner : AbstractQueryTranslator) {
	val logger = Logger.getLogger("R2RMLFusionTablesBetaGenerator");
	val dbType = this.owner.getDatabaseType();
	
	override def calculateBetaSubject(tp:Triple, cm:AbstractConceptMapping , alphaResult:AlphaResult ) 
	: java.util.List[ZSelectItem] = {
		var result:List[ZSelectItem] = Nil;
		val triplesMap = cm.asInstanceOf[R2RMLTriplesMap];
		val logicalTableAlias = alphaResult.getAlphaSubject().getAlias();
		val tpSubject = tp.getSubject();
		
		val subjectMap = triplesMap.getSubjectMap();
		if(tpSubject.isVariable()) {
			val varNameHashCode = tpSubject.getName().hashCode();
			var mapHashCodeMapping = this.getOwner().getMapHashCodeMapping();
			mapHashCodeMapping.put(varNameHashCode , subjectMap);
		}
		
		val databaseColumnsString = subjectMap.getDatabaseColumnsString();
		if(databaseColumnsString != null) {
			for(databaseColumnString <- databaseColumnsString) {
				val selectItem = MorphSQLSelectItem.apply(
				    databaseColumnString, logicalTableAlias, dbType, null);
				result = result ::: List(selectItem);
			}
		}
		
		result;
	}


	override def calculateBetaObject(tp:Triple, cm:AbstractConceptMapping , predicateURI:String ,
			alphaResult:AlphaResult , pm:AbstractPropertyMapping ) : java.util.List[ZSelectItem] = {
		val predicateObjectMap = pm.asInstanceOf[R2RMLPredicateObjectMap];
		val refObjectMap = predicateObjectMap.getRefObjectMap(); 
		var betaObjects:List[ZSelectItem] = Nil;
		val logicalTableAlias = alphaResult.getAlphaSubject().getAlias();
		val tpObject = tp.getObject();


					
		if(refObjectMap == null) {
			val objectMap = predicateObjectMap.getObjectMap();
			if(tpObject.isVariable()) {
				val varNameHashCode = tpObject.getName().hashCode();
				var mapHashCodeMapping = this.getOwner().getMapHashCodeMapping();
				mapHashCodeMapping.put(varNameHashCode , objectMap);
			}
					
			if(objectMap.getTermMapType() == TermMapType.CONSTANT) {
				val constantValue = objectMap.getConstantValue();
				val zConstant = new ZConstant(constantValue, ZConstant.STRING);
				val selectItem = MorphSQLSelectItem.apply(zConstant);
				betaObjects.add(selectItem);
			} else {
				val databaseColumnsString = objectMap.getDatabaseColumnsString();
				for(databaseColumnString <- databaseColumnsString) {
					val selectItem = MorphSQLSelectItem.apply(
							databaseColumnString,logicalTableAlias, dbType, null);
					
					betaObjects = betaObjects ::: List(selectItem);
				}
			}
		} else {
			if(tpObject.isVariable()) {
				val varNameHashCode = tpObject.getName().hashCode();
				var mapHashCodeMapping = this.getOwner().getMapHashCodeMapping();
				mapHashCodeMapping.put(varNameHashCode , refObjectMap);
			}
			
			val databaseColumnsString = refObjectMap.getParentDatabaseColumnsString();
			val refObjectMapAlias = this.owner.asInstanceOf[R2RMLQueryTranslator].getMapTripleAlias().get(tp);

			if(databaseColumnsString != null) {
				for(databaseColumnString <- databaseColumnsString) {
					val selectItem = MorphSQLSelectItem.apply(
							databaseColumnString, refObjectMapAlias, dbType, null);
					betaObjects.add(selectItem);
				}
			}
		}	
		return betaObjects;
	}
	
}