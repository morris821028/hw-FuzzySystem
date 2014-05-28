package MarathonRound2;

import java.util.Random;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import calcModel.geneAlgorithm.Gene;
import calcModel.geneAlgorithm.ui.GeneControl;

public class RectanglesAndHoles {
	public static RectanglesAndHolesVis vis = new RectanglesAndHolesVis();
	public static int N = 100;
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
				A[i] = generator.nextInt(50) + 50;
				B[i] = generator.nextInt(200) + 50;
				if(Math.random() < 0.5) {
					int t;
					t = A[i];
					A[i] = B[i];
					B[i] = t;
					kind[i] = 0;
				}
			}
			
			FloorPlanTool.getRandomPlace(A, B);
			for (int i = 0; i < N; i++) {
				LX[i] = FloorPlanTool.LX[i];
				LY[i] = FloorPlanTool.LY[i];
				kind[i] = 0;
			}

			long score = vis.runTest(N, A, B, LX, LY, kind);
			
			Gene.DNALength = 3 * N;
			
			GeneControl.getInstance().restartMachine(
					GeneControl.getInstance().getBestGene());
			

			System.out.println("Score  = " + score);
			
		} catch (RuntimeException e) {
			System.err
					.println("ERROR: Unexpected error while running your test case.");
			e.printStackTrace();
			RectanglesAndHolesVis.stopSolution();
		}
	}
}
