package org.k1s.petriCode.generationVisitors	

import org.k1s.petriCode.blocks.Principal;


/**
 * Variables visitos pass 1
 * @author kent
 *
 */
class VarsVisitor extends ODGVisitor{
	def bindings
	
	def visitElement(element, bindings){
		
		//1. Find vars
	    //2. report to container
		//3. if container, report to self
		def varNames = getVarNames(element)
		//remove vars tag
		element.text = element.text.replaceAll("%%VARS:.*%%","" )
		
		/*if(element instanceof Container){
			element.declarations.addAll varNames
			
		}else if(element.parent != null)*/
		//println "VARS_VISITOR: adding ${varNames} to ${element.parent?.correspondingNetElement?.name?.text}"
		//println element
		//println "element.metaClass.hasProperty(element, \"parent\") && element.parent: ${element.metaClass.hasProperty(element,"parent")} && ${element.metaClass.hasProperty(element, "parent") ? element.parent: 'no such field'}"
		if(element.metaClass.hasProperty(element, "parent") && element.parent && !(element.parent instanceof Principal)){
			//println "adding vars to ${element.parent}"
			element.parent.declarations += varNames	
		}
		
		
	}
	
	def getVarNames(element) {
		//println element
		
		def allVars = []
		def vars = element.text.indexOf( "%%VARS:" )
		while(vars > -1 ) {
			vars = vars + 7
		//else return []
		
			def endVars = element.text.indexOf( "%%", vars)
			allVars +=  element.text.substring(vars, endVars).split(",").collect { it.trim() }.findAll { it != "" }
			vars = element.text.indexOf( "%%VARS:", vars )
		}
		
		def retval = [:]
		
		allVars.each{
			it = it.trim()
			if(it.contains("::")){
				retval[it.substring(0,it.indexOf("::")).trim()] = it.substring(it.indexOf("::")+2).trim()
			} else {
				retval[it] = ""
			}
			
		}
		
		return retval
	}
}
