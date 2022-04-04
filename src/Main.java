import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import javax.swing.JFileChooser;
public class Main {
	public static int getInput(int startMenuNumber, int endMenuNumber, String message) { // fungsi untuk menerima input
		System.out.print(message);
		
		Scanner scanner = new Scanner(System.in);						
		int command = scanner.nextInt();
		while(command < startMenuNumber || command > endMenuNumber) {		// selama input tidak sesuai dengan angka yang diinginkan
			System.out.println("Masukan Tidak Valid! Coba lagi");			// print pesan kesalahan
			System.out.print(message);
			command = scanner.nextInt();
		}
		System.out.println();
		scanner.close();
		return command;
	}
	
	public static String chooseFile() {										// fungsi untuk memilih file yang ingin diselesaikan
		SimpanPilihFile fc = new SimpanPilihFile();
		String lastSelectedFileDirectory =  System.getProperty("user.dir");
		fc.setDialogTitle("Pilih File");
		fc.setCurrentDirectory(new File(lastSelectedFileDirectory));
		
		int response = fc.showOpenDialog(null);
		if(response == JFileChooser.APPROVE_OPTION) {						// Jika file yang di klik sesuai
			String path = fc.getSelectedFile().getPath();					// akan mendapatkan path dari file tersebut untuk dibuka
			lastSelectedFileDirectory = path;
			return path;
		} else {
			return "";
		}
	}
	public static puzzle readPuzzle(String path) {						// Fungsi untuk membaca puzzle dari sebuah file
		
		File sourceFile = new File(path);
		puzzle P = new puzzle();
		
		Scanner scanner;
		try {
			scanner = new Scanner(sourceFile);
			String baris;
			int x = 0;
			while(scanner.hasNextLine()) {								// Selama belum terbaca enter 
				baris = scanner.nextLine();
				List<Integer> row = new ArrayList<Integer>();
				String[] barisArr = baris.split(" ");
				for (int j = 0; j < barisArr.length; j++) {
					row.add(Integer.parseInt(barisArr[j]));				// Ubah hasil input dari string ke integer
					P.setValue((4*x)+j,row.get(j));						// Masukkan integer tersebut ke dalam objek puzzle
				}
				System.out.println();
				x++;
			}
			scanner.close();
		} catch (FileNotFoundException e) {								//exception jika file yang dipilih tidak ada
			e.printStackTrace();
		}
		return P;
	}
	public static void printStep(Stack<puzzle> step) {					// Fungsi untuk mencetak langkah-langkah puzzle dari awal hingga selesai
		int i =0;
		while(step.size()>0) {
			System.out.printf("Step %d\n",i+1);
			puzzle Puzzle = step.pop();									// Keluarkan puzzle dari stack
			Puzzle.printPuzzle();										// Print puzzle dengan method printPuzzle
			i++;
		}
	}
	public static void main(String[] args) {							// Fungsi utama
		puzzle Puzzle = new puzzle();
		int command = 0;
		System.out.printf("Pilih angka untuk input Puzzle: \n1. Secara Acak\n2. Dari input file \n");
		String masukkan = "Masukkan: ";
		command = getInput(1,2,masukkan);								//Memanggil fungsi getInput untuk memasukkan input yang diberikan
		if (command == 1) {
			Puzzle.acak();												//Jika input adalah satu akan memanggil method acak untuk mengacak puzzle
		}
		if(command ==2) {
			String filePath;											//Jika input adalah dua maka akan memanggil fungsi chooseFile()
			try {
				filePath = chooseFile();
				if(filePath.isEmpty()) {								//Jika file path kosong akan mengeluarkan pesan error
					System.out.println("Tidak ada file yang dipilih");
				}
				Puzzle = readPuzzle(filePath);							//Memanggil fungsi readPuzzle sesuai dengan path file yang dipilih
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("Puzzle yang akan diselesaikan: ");
		Puzzle.printPuzzle();											//Mencetak puzzle yang akan diselesaikan
		int goal = Puzzle.getGoal();									//Menghitung goal dan mencetak fungsi KURANG(i) dengan method getGoal untuk melihat apakah puzzle dapat diselesaikan
		System.out.printf("Total nilai KURANG(i) + X : %d\n",goal);		
		if(goal%2 != 0) {												//Jika goal ganjil maka puzzle tidak dapat diselesaikan dan program akan berhenti
			System.out.println("Puzzle tidak dapat diselesaikan");
			return;
		}
		else {															//Jika goal genap maka puzzle bisa dilesaikan
			System.out.println("Puzzle bisa diselesaikan");
			long start = System.nanoTime();								//Menyimpan waktu dimulai perhitungan ke dalam variabel start
			Solve solve = new Solve();									
			Stack<puzzle> step = new Stack<puzzle>();					//Membuat objek step dengan class stack untuk menyimpan langkah-langkah puzzle
			int bangkit  = solve.solvePuzzle(Puzzle,step);				// memanggil fungsi solvePuzzle untuk menyelesaikan puzzle dan mengembalikan jumlah simpul yang dibangkitkan ke variabel bangkit
			if(bangkit>5000000 && !step.peek().solved()) {				//Jika belum selesai mencoba diselesaikan lagi dengan simpul terakhir yang dipilih
				Puzzle = step.get(0);
				step.removeAll(step);
				bangkit = bangkit + solve.solvePuzzle(Puzzle, step);
			}
			long finish = System.nanoTime();							//Menyimpan waktu berakhirnya perhitungan ke variabel finish
			printStep(step);											//Memanggil fungsi printStep untuk mencetak langkah-langkah penyeselesaian puzzle
			System.out.printf("Jumlah simpul yang dibangkitkan: %d\n", bangkit);					//Mencetak jumlah simpul yang dibangkitkan
			System.out.printf("Lama Waktu Algoritma: %d nano seconds\n", finish-start);				//Mencetak lama Waktu algoritma dalam nanosecond, milisecond, dan second
			System.out.printf("Lama Waktu Algoritma: %d mili seconds\n", (finish-start)/1000000);
			System.out.printf("Lama Waktu Algoritma: %d seconds\n", (finish-start)/1000000000);
		}
		
		
	}
}
