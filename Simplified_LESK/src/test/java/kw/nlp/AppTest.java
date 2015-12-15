package kw.nlp;

import edu.mit.jwi.item.IWord;
import junit.framework.Assert;
import junit.framework.TestCase;


/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {

	@SuppressWarnings("deprecation")
	public void testSimplifiedLesk() throws Exception {

SimplifiedLesk sl = new SimplifiedLesk(this.getClass().getResource("/dict_3.0").getPath());
IWord sense = sl.implementLesk("The bank can guarantee deposits will eventually cover future tuition costs because it invests in adjustable-rate mortgage securities", "bank");
Assert.assertEquals(sense.getSynset().getGloss().trim().toLowerCase(), "a financial institution that accepts deposits and channels the money into lending activities; \"he cashed a check at the bank\"; \"that bank holds the mortgage on my home\"");

	}
}
