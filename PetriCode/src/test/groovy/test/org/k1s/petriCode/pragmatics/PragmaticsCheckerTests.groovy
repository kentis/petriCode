package org.k1s.petriCode.pragmatics;

import static org.junit.Assert.*;

import org.junit.Test;
import org.k1s.petriCode.pragmatics.PragmaticsChecker;
import org.k1s.petriCode.pragmatics.PragmaticsConstraints;
import org.k1s.petriCode.pragmatics.PrgamaticsDescriptorDSL
import org.k1s.petriCode.att.ATTFactoryTests;
import org.k1s.petriCode.blocks.derived.PragmaticsDerivator;
import org.k1s.petriCode.cpn.io.CpnIO
import org.k1s.petriCode.generation.CodeGeneratorTests;

import static org.hamcrest.CoreMatchers.*
import org.hamcrest.core.IsNull;

class PragmaticsCheckerTests {
	@Test
	void testMessageSendingProtocol(){
		//fail "NYW"
		
		def model = this.class.getResourceAsStream("/ProtocolModel.cpn")
		def pragmaticsDescriptor = CodeGeneratorTests.getPragmaticsDesciptors()
		def io = new CpnIO(pragmaticsDescriptor)
		def cpn = io.readCPN(model)
		io.parsePragmatics(cpn)
		
		PragmaticsDerivator.addDerivedPragmatics(cpn, ATTFactoryTests.getCorePragmatics())
		
		assertTrue PragmaticsChecker.check(cpn, ATTFactoryTests.getCorePragmatics() )
	}
	
	
	@Test
	void testMessageSendingProtocolWrongPragDesc(){
		//fail "NYW"
		
		def model = this.class.getResourceAsStream("/ProtocolModel.cpn")
		def pragmaticsDescriptor = CodeGeneratorTests.getPragmaticsDesciptors()
		def io = new CpnIO(pragmaticsDescriptor)
		def cpn = io.readCPN(model)
		io.parsePragmatics(cpn)
		
		
		
		PragmaticsDerivator.addDerivedPragmatics(cpn, ATTFactoryTests.getCorePragmatics())
		
		
		def corePrags = ATTFactoryTests.getCorePragmatics() 
		
		PragmaticsConstraints constraint = new PragmaticsConstraints()
		constraint.levels = 'principal'
		constraint.connectedTypes = 'Place'
		corePrags.principal.constraints = constraint
		corePrags.Principal.constraints = constraint
		
		def violations = []
		def res = PragmaticsChecker.check(cpn, corePrags, violations)
		println violations
		assertFalse res
	}
	
	@Test
	void testMessageSendingProtocolWrongPragDescLevel(){
		//fail "NYW"
		
		def model = this.class.getResourceAsStream("/ProtocolModel.cpn")
		def pragmaticsDescriptor = CodeGeneratorTests.getPragmaticsDesciptors()
		def io = new CpnIO(pragmaticsDescriptor)
		def cpn = io.readCPN(model)
		io.parsePragmatics(cpn)
		
		
		
		PragmaticsDerivator.addDerivedPragmatics(cpn, ATTFactoryTests.getCorePragmatics())
		
		
		
		
		String prags = """
		principal(origin: 'explicit', constraints: [levels: 'service', connectedTypes: 'SubstitutionTransition'])
		channel(origin: 'explicit')
		Id(origin: 'explicit', controlFlow: true, constraints: [levels: 'service', connectedTypes: 'Place'])
		LCV(origin: 'explicit')
		"""
		
		def corePrags = getTestPragmatics(prags)
		
		
		assertFalse PragmaticsChecker.check(cpn, corePrags )
		
	}
	
	
	@Test
	void testReadCorePragsConstraints(){
		def prags = getTestPragmatics()
		
		assertThat prags.Principal.constraints, not(new IsNull())
		assertThat prags.Principal.constraints.levels, is("protocol")
		assertThat prags.Principal.constraints.connectedTypes, is("SubstitutionTransition")
		
		
		assertThat prags.channel.constraints, new IsNull()
	
		
	}
	
	def getTestPragmatics(){
		
		String prags = """
		Principal(origin: 'explicit', constraints: [levels: 'protocol', connectedTypes: 'SubstitutionTransition'])
		channel(origin: 'explicit')
		Id(origin: 'explicit', controlFlow: true, constraints: [levels: 'service', connectedTypes: 'Place'])
		LCV(origin: 'explicit')
		"""
		
		return getTestPragmatics(prags)
	}
	
	def getTestPragmatics(String prags){
		def desc = new PrgamaticsDescriptorDSL()
		desc.build(prags)
		return desc.prags
	}
}
