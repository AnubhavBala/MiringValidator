/*

    MiringValidator  Semantic Validator for MIRING compliant HML
    Copyright (c) 2014-2015 National Marrow Donor Program (NMDP)

    This library is free software; you can redistribute it and/or modify it
    under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation; either version 3 of the License, or (at
    your option) any later version.

    This library is distributed in the hope that it will be useful, but WITHOUT
    ANY WARRANTY; with out even the implied warranty of MERCHANTABILITY or
    FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
    License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this library;  if not, write to the Free Software Foundation,
    Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA.

    > http://www.gnu.org/licenses/lgpl.html

*/
package test.java.miringvalidatortest;

import static org.junit.Assert.*;
import main.java.miringvalidator.MiringValidator;
import main.java.miringvalidator.ReportGenerator;
import main.java.miringvalidator.SchematronValidator;
import main.java.miringvalidator.Utilities;
import main.java.miringvalidator.ValidationError;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Test;

public class MiringTier2Test
{
    private static final Logger logger = LogManager.getLogger(MiringTier2Test.class);

    @Test
    public void testTemp()
    {
        //5.3.b and 5.3.c
        /*String xml = Utilities.readXmlResource("/hml/Element5.variant.good.ids.xml");
        String results = new MiringValidator(xml).validate();
        System.out.println(results);
        assertFalse(Utilities.containsErrorNode(results, "The variant nodes under a single consensus-sequence-block must have id attributes that are integers ranging from 0:n-1, where n is the number of variants"));
        
        xml = Utilities.readXmlResource("/hml/Element5.variant.bad.ids.1.xml");
        results = new MiringValidator(xml).validate();
        System.out.println(results);
        assertTrue(Utilities.containsErrorNode(results, "The variant nodes under a single consensus-sequence-block must have id attributes that are integers ranging from 0:n-1, where n is the number of variants"));

        xml = Utilities.readXmlResource("/hml/Element5.variant.bad.ids.2.xml");
        results = new MiringValidator(xml).validate();
        System.out.println(results);
        assertTrue(Utilities.containsErrorNode(results, "The variant nodes under a single consensus-sequence-block must have id attributes that are integers ranging from 0:n-1, where n is the number of variants"));

        xml = Utilities.readXmlResource("/hml/Element5.variant.bad.ids.3.xml");
        results = new MiringValidator(xml).validate();
        System.out.println(results);
        assertTrue(Utilities.containsErrorNode(results, "The variant nodes under a single consensus-sequence-block must have id attributes that are integers ranging from 0:n-1, where n is the number of variants"));
*/
        //TODO

    }

    @Test
    public void testMiringElement1Tier2()
    {
        logger.debug("starting testMiringElement1Tier2");

        String xml;
        ValidationError[] errors; 
        
        //1.1.c
        //Test if HMLID root is an OID
        xml = Utilities.readXmlResource("/hml/Element1.hmlid.OID.xml");
        errors = SchematronValidator.validate(xml,new String[]{"/schematron/MiringElement1.sch"});
        String oidHmlidErrorReport = ReportGenerator.generateReport(errors,Utilities.getHMLIDRoot(xml), Utilities.getHMLIDExtension(xml));
        
        xml = Utilities.readXmlResource("/hml/Element1.hmlid.not.OID.xml");
        errors = SchematronValidator.validate(xml,new String[]{"/schematron/MiringElement1.sch"});
        String notOidHmlidErrorReport = ReportGenerator.generateReport(errors,Utilities.getHMLIDRoot(xml), Utilities.getHMLIDExtension(xml));

        assertFalse(Utilities.containsErrorNode(oidHmlidErrorReport, "The hmlid root is not formatted like an OID."));
        assertTrue(Utilities.containsErrorNode(oidHmlidErrorReport, "The hmlid root is formatted like an OID."));
        
        assertTrue(Utilities.containsErrorNode(notOidHmlidErrorReport, "The hmlid root is not formatted like an OID."));
        assertFalse(Utilities.containsErrorNode(notOidHmlidErrorReport, "The hmlid root is formatted like an OID."));

        //1.3.b
        xml = Utilities.readXmlResource("/hml/Element1.valid.testidsource.xml");
        errors = SchematronValidator.validate(xml,new String[]{"/schematron/MiringElement1.sch"});
        String validTestIDErrorReport = ReportGenerator.generateReport(errors,Utilities.getHMLIDRoot(xml), Utilities.getHMLIDExtension(xml));
        
        xml = Utilities.readXmlResource("/hml/Element1.invalid.testidsource.xml");
        errors = SchematronValidator.validate(xml,new String[]{"/schematron/MiringElement1.sch"});
        String invalidTestIDErrorReport = ReportGenerator.generateReport(errors,Utilities.getHMLIDRoot(xml), Utilities.getHMLIDExtension(xml));

        assertFalse(Utilities.containsErrorNode(validTestIDErrorReport, "On a sbt-ngs node, test-id is not formatted like a GTR test ID."));
        assertFalse(Utilities.containsErrorNode(validTestIDErrorReport, "On a sbt-ngs node, the test-id-source is not explicitly 'NCBI-GTR'."));
        
        assertTrue(Utilities.containsErrorNode(invalidTestIDErrorReport, "On a sbt-ngs node, test-id is not formatted like a GTR test ID."));
        assertTrue(Utilities.containsErrorNode(invalidTestIDErrorReport, "On a sbt-ngs node, the test-id-source is not explicitly 'NCBI-GTR'."));
    }

    @Test
    public void testMiringElement2Tier2()
    {
        logger.debug("starting testMiringElement2Tier2");
        
        //2.2.c
        String xml = Utilities.readXmlResource("/hml/Element2.referencesequence.good.startend.xml");
        String results = new MiringValidator(xml).validate();
        assertFalse(Utilities.containsErrorNode(results, "On a reference sequence node, end attribute should be greater than or equal to the start attribute."));
        
        xml = Utilities.readXmlResource("/hml/Element2.referencesequence.bad.startend.xml");
        results = new MiringValidator(xml).validate();
        assertTrue(Utilities.containsErrorNode(results, "On a reference sequence node, end attribute should be greater than or equal to the start attribute."));
        
        //2.2.1.c
        xml = Utilities.readXmlResource("/hml/Element2.refsequence.nomatch.csb.xml");
        String badResults = new MiringValidator(xml).validate();

        xml = Utilities.readXmlResource("/hml/Element2.refsequence.match.csb.xml");
        String goodResults = new MiringValidator(xml).validate();

        assertTrue(Utilities.containsErrorNode(badResults, "A reference-sequence node has an id attribute with no corresponding consensus-sequence-block id attribute."));
        assertFalse(Utilities.containsErrorNode(goodResults, "A reference-sequence node has an id attribute with no corresponding consensus-sequence-block id attribute."));

    }

    @Test
    public void testMiringElement3Tier2()
    {
        logger.debug("starting testMiringElement3Tier2");
        logger.debug("Nothing tested in Element 3 yet.");
    }

    @Test
    public void testMiringElement4Tier2()
    {
        logger.debug("starting testMiringElement4Tier2");
        
        //TODO: 
        //4.a
        //fail("not implemented yet.");
        //4.b
        //fail("not implemented yet.");
        
        //4.2.3.b
        String xml = Utilities.readXmlResource("/hml/Element4.CSB.good.startend.xml");
        String results = new MiringValidator(xml).validate();
        assertFalse(Utilities.containsErrorNode(results, "On a consensus-sequence-block node, end attribute should be greater than or equal to the start attribute."));
        
        xml = Utilities.readXmlResource("/hml/Element4.CSB.bad.startend.xml");
        results = new MiringValidator(xml).validate();
        assertTrue(Utilities.containsErrorNode(results, "On a consensus-sequence-block node, end attribute should be greater than or equal to the start attribute."));
        
        //4.2.3.d
        xml = Utilities.readXmlResource("/hml/Element4.CSB.within.refseq.xml");
        results = new MiringValidator(xml).validate();        
        assertFalse(Utilities.containsErrorNode(results, "The start attribute on a consensus-sequence-block node should be greater than or equal to the start attribute on the corresponding reference-sequence node."));
        assertFalse(Utilities.containsErrorNode(results, "The end attribute on a consensus-sequence-block node should be less than or equal to the end attribute on the corresponding reference-sequence node."));
        
        xml = Utilities.readXmlResource("/hml/Element4.CSB.outside.refseq.1.xml");
        results = new MiringValidator(xml).validate();
        assertFalse(Utilities.containsErrorNode(results, "The start attribute on a consensus-sequence-block node should be greater than or equal to the start attribute on the corresponding reference-sequence node."));
        assertTrue(Utilities.containsErrorNode(results, "The end attribute on a consensus-sequence-block node should be less than or equal to the end attribute on the corresponding reference-sequence node."));
        
        xml = Utilities.readXmlResource("/hml/Element4.CSB.outside.refseq.2.xml");
        results = new MiringValidator(xml).validate();
        assertTrue(Utilities.containsErrorNode(results, "The start attribute on a consensus-sequence-block node should be greater than or equal to the start attribute on the corresponding reference-sequence node."));
        assertFalse(Utilities.containsErrorNode(results, "The end attribute on a consensus-sequence-block node should be less than or equal to the end attribute on the corresponding reference-sequence node."));
        
        xml = Utilities.readXmlResource("/hml/Element4.CSB.outside.refseq.3.xml");
        results = new MiringValidator(xml).validate();
        assertTrue(Utilities.containsErrorNode(results, "The start attribute on a consensus-sequence-block node should be greater than or equal to the start attribute on the corresponding reference-sequence node."));
        assertTrue(Utilities.containsErrorNode(results, "The end attribute on a consensus-sequence-block node should be less than or equal to the end attribute on the corresponding reference-sequence node."));
        
        //4.2.3.e
        xml = Utilities.readXmlResource("/hml/Element4.CSB.good.sequencelength.xml");
        results = new MiringValidator(xml).validate();
        assertFalse(Utilities.containsErrorNode(results, "For every consensus-sequence-block node, the child sequence node must have a length of (end - start)."));
        
        xml = Utilities.readXmlResource("/hml/Element4.CSB.bad.sequencelength.xml");
        results = new MiringValidator(xml).validate();
        assertTrue(Utilities.containsErrorNode(results, "For every consensus-sequence-block node, the child sequence node must have a length of (end - start)."));

        //4.2.4.b
        xml = Utilities.readXmlResource("/hml/Element4.phasinggroup.xml");
        results = new MiringValidator(xml).validate();
        assertTrue(Utilities.containsErrorNode(results, "On a consensus-sequence-block node, the phasing-group attribute is deprecated."));
        
        //4.2.7.b
        xml = Utilities.readXmlResource("/hml/Element4.CSB.continuous.xml");
        results = new MiringValidator(xml).validate();
        assertFalse(Utilities.containsErrorNode(results, "A consensus-sequence-block with attribute continuity=\"true\" does not appear to be continuous with it's previous sibling consensus-sequence-block node,"));
        
        xml = Utilities.readXmlResource("/hml/Element4.CSB.not.continuous.xml");
        results = new MiringValidator(xml).validate();
        assertTrue(Utilities.containsErrorNode(results, "A consensus-sequence-block with attribute continuity=\"true\" does not appear to be continuous with it's previous sibling consensus-sequence-block node,"));
        
    }

    @Test
    public void testMiringElement5Tier2()
    {
        logger.debug("starting testMiringElement5Tier2");
        
        //5.2.b and 5.2.d
        String xml = Utilities.readXmlResource("/hml/Element5.variant.bad.attributes.xml");
        String badResults = new MiringValidator(xml).validate();
        System.out.println(badResults);
        assertFalse(Utilities.containsErrorNode(badResults, "The start attribute on a variant node should be greater than or equal to the start attribute on the corresponding reference-sequence node."));
        assertTrue(Utilities.containsErrorNode(badResults, "The end attribute on a variant node should be less than or equal to the end attribute on the corresponding reference-sequence node."));
        assertTrue(Utilities.containsErrorNode(badResults, "On a variant node, end attribute should be greater than or equal to the start attribute."));

        xml = Utilities.readXmlResource("/hml/Element5.variant.good.attributes.xml");
        String goodResults = new MiringValidator(xml).validate();
        System.out.println(goodResults);
        assertFalse(Utilities.containsErrorNode(goodResults, "The start attribute on a variant node should be greater than or equal to the start attribute on the corresponding reference-sequence node."));
        assertFalse(Utilities.containsErrorNode(goodResults, "The end attribute on a variant node should be less than or equal to the end attribute on the corresponding reference-sequence node."));
        assertFalse(Utilities.containsErrorNode(goodResults, "On a variant node, end attribute should be greater than or equal to the start attribute."));


    }
}