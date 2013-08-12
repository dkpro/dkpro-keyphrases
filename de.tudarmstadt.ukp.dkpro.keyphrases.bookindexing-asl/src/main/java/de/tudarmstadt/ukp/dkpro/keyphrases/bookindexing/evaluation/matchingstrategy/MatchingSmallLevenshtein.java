package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.evaluation.matchingstrategy;

public class MatchingSmallLevenshtein
	extends MatchingStrategy
{

	@Override
	public boolean isMatch(String phrase, String goldPhrase)
	{
		return smallLevenshtein(phrase, goldPhrase);
	}

	/**
	 * Consumes two strings and returns true if the strings are within a tolerable
	 * small Levenshtein-distance.
	 *
	 * @param s1
	 * @param s2
	 * @return True, if s1 has a tolerable small Levenshtein distance to s2.
	 */
	public static boolean smallLevenshtein(String s1, String s2)
	{
		s1 = normalizeString(s1);
		s2 = normalizeString(s2);

		if (Math.abs(s1.length() - s2.length()) > 2) {
			return false;
		}

		String difference = org.apache.commons.lang.StringUtils.difference(s1, s2);

		switch (difference.length()) {
		case 0:
			return true;
		case 1:
			// for english plural
			return difference.equals("s");
		default:
			return false;
		}
	}

	/**
	 * The normalized form of the string is lowercase and contains no punctuation,
	 * no trailing and no successive whitespace.
	 * <p>
	 * The normalization is a bit rough, f.e. "Henry's son" becomes "henrys son".
	 *
	 * @param string
	 *          to be normalized
	 * @return
	 */
	private static String normalizeString(String string)
	{

		string = string.replaceAll("[.!?'\"/]", "");
		string = string.replaceAll("[\\n\\r\\t\\f-/_]", " ");
		string = string.replaceAll(" {2,}", " ");
		string = string.trim();
		return string.toLowerCase();
	}

	@Override
	public String toString()
	{
		return getClass().getSimpleName();
	}

}
