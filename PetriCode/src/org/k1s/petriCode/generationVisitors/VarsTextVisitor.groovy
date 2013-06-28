package org.k1s.petriCode.generationVisitors	

import org.k1s.petriCode.blocks.Principal;
import org.k1s.petriCode.blocks.Service
import org.k1s.petriCode.generation.CodeGenerator

import groovy.text.SimpleTemplateEngine;


/**
 * Variables visitor pass 2
 * @author kent
 *
 */
class VarsTextVisitor extends ODGVisitor{
	def bindings
	
	def visitElement(element, bindings){
		
		//set declarationText on all Containers
		if(element.metaClass.hasProperty(element, "declarationsText")){
			SimpleTemplateEngine engine = new SimpleTemplateEngine()

			def template = bindings.bindings['DECLARATIONS']
			
			if(template == null){
				throw new RuntimeException("DECLARATIONS template not found.")
			}
			
			def decls = element.declarations.unique()
			if(element.metaClass.hasProperty(element, 'parent') && element.parent && !(element.parent instanceof Principal)){
				def parent  = element.parent
				while(parent && !(parent instanceof Principal)){
					//remove previously defined variables
					decls = decls - parent.declarations
					
					if(parent instanceof Service){
						parent.node.pragmatics[0].getArguments().split(",").each{
							
							decls = decls - it.trim()
						
						}
						
					}
					
					parent = parent.parent
					
				}
				if(parent instanceof Principal){
					def principalDecls = []
					parent.states.each{
						principalDecls << CodeGenerator.removePrags(it.name.text.toString()).trim()
					}
					decls = decls - principalDecls
				}
				
				
			}
			
			if(element instanceof Service){
				element.node.pragmatics[0].getArguments().split(",").each{
					//println "removing $it from $decls"
					decls = decls - it.trim()
					//println "leaving $decls"
				}
				
			}
			
			def text = engine.createTemplate(new File(template.template)).make([vars: decls, indentLevel: element.level])
			
			element.declarationsText = text.toString()
		}
	}

}
