package calcModel.fuzzySystem;

public class FuzzySystemFactory {
	public static FuzzySystem createFuzzySystem(String args) {
		if(args.equals("Functional"))
			return new FuzzySystem();
		else if(args.equals("CenterOfGravity"))
			return new FuzzySystemII();
		else if(args.equals("MeanOfMax"))
			return new FuzzySystemIII();
		else if(args.equals("(Modified)Mean"))
			return new FuzzySystemIV();
		return null;
	}
}
