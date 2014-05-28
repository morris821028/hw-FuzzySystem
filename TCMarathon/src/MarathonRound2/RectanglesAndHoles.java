package MarathonRound2;

import java.util.Random;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import calcModel.geneAlgorithm.Gene;
import calcModel.geneAlgorithm.ui.GeneControl;

public class RectanglesAndHoles {
	public static RectanglesAndHolesVis vis = new RectanglesAndHolesVis();
	public static int N = 20;
	public static int[] A = new int[N];
	public static int[] B = new int[N];
	public static int[] LX = new int[N];
	public static int[] LY = new int[N];
	public static int[] kind = new int[N];
	
	public static int[] place(int[] A, int[] B) {
		return null;
	}

	public static void main(String[] args) {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			// If Nimbus is not available, you can set the GUI to another look
			// and feel.
		}
		
		try {

			Random generator = new Random();

			for (int i = 0; i < N; i++) {
				A[i] = generator.nextInt(50) + 1;
				B[i] = generator.nextInt(300) + 50;
				LX[i] = generator.nextInt(300) + 1;
				LY[i] = generator.nextInt(300) + 1;
				kind[i] = generator.nextInt(2);
			}

			Gene.DNALength = 3 * N;
			
			GeneControl.getInstance().restartMachine(
					GeneControl.getInstance().getBestGene());
			
			long score = vis.runTest(N, A, B, LX, LY, kind);

			System.out.println("Score  = " + score);
			
		} catch (RuntimeException e) {
			System.err
					.println("ERROR: Unexpected error while running your test case.");
			e.printStackTrace();
			RectanglesAndHolesVis.stopSolution();
		}
	}
}
