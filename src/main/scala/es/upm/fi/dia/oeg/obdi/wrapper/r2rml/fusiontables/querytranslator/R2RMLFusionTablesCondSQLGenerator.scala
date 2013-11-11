package es.upm.fi.dia.oeg.obdi.wrapper.r2rml.fusiontables.querytranslator

import es.upm.fi.dia.oeg.obdi.core.querytranslator.AbstractCondSQLGenerator
import es.upm.fi.dia.oeg.obdi.core.querytranslator.AbstractQueryTranslator
import Zql.ZExp
import Zql.ZExpression
import Zql.ZConstant
import com.hp.hpl.jena.graph.Node
import es.upm.fi.dia.oeg.obdi.core.querytranslator.AlphaResult
import es.upm.fi.dia.oeg.obdi.core.querytranslator.BetaResult
import es.upm.fi.dia.oeg.obdi.core.model.AbstractConceptMapping
import es.upm.fi.dia.oeg.obdi.wrapper.r2rml.rdb.querytranslator.R2RMLCondSQLGenerator

class R2RMLFusionTablesCondSQLGenerator(owner : AbstractQueryTranslator) 
extends R2RMLCondSQLGenerator(owner : AbstractQueryTranslator) {

	override def generateIsNotNullExpression(betaObjectExpression : ZExp) : ZExpression = {
		val exp = new ZExpression("NOT EQUAL TO");
		exp.addOperand(betaObjectExpression);
		val emptyString = new ZConstant("", ZConstant.STRING);
		exp.addOperand(emptyString);
		
		//exp = null;
		return exp;
	}
	

}