package org.k1s.petriCode.pragmatics

import javax.management.InstanceOfQueryExp;



/**
 * Defines the descriptor (type) information of a pragmatic.
 * @author kent
 *
 */
class PragmaticsDescriptor {

	OriginType origin = OriginType.EXPLICIT
	String name
	List paramters
	PragmaticsConstraints constraints
	
	List derviationRules
	//List validationRules
	
	def conditional

	//indicates wether or not this pragatic idicates that the underlying element
	//is a service layer page
	boolean containsService = false
	
	//indicates wether or not this pragatic indicates the control flow path
	boolean controlFlow = false
	Map block
	
	void setOrigin(origin){
		if(origin instanceof String){
			if(origin == "explicit"){
				origin = OriginType.EXPLICIT
			} else if(origin == "derived"){
			origin = OriginType.DERIVED
			}
			
		}
		this.origin = origin
	}

	void setConstraints(c){
		switch(c){
			case PragmaticsConstraints:
				constraints = c
				break;
			case List:
			
				break;
			case Map:
				constraints = new PragmaticsConstraints(c)
				break;
			default:
				throw new Exception("can not construct PragmaticConstraint from: ${c.class}")
		}

	}
		
	
	String toString(){
		return "$name($paramters)"
	}
	
	boolean equals(PragmaticsDescriptor pragDesc){
		
		this.name.equals(pragDesc.name)
	}
	
	int hashCode(){
		
		this.name.hashCode()
	}
}

enum OriginType {
	EXPLICIT, DERIVED
}

/*class ParamterDesciptor {
	String name
	String type
	List<String> legalValues
}*/
