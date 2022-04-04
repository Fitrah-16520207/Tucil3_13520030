import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
public class Solve {
	public void swap(puzzle Puzzle, String direction) {					//Fungsi swap untuk mengganti ubin yang kosong sesuai dengan arah yang diingkan
		int change = Puzzle.getValue(Puzzle.getEmpty());
		if(direction.equals("left")) {									//Jika arah yang diganti adalah "left" atau kiri maka ubin yang kosong akan ditukar dengan sebelah kirinya
			if(Puzzle.empty != 0 && Puzzle.empty  != 4 && Puzzle.empty  != 8 && Puzzle.empty  != 12) {
				Puzzle.setValue(Puzzle.empty, Puzzle.getValue(Puzzle.empty-1));
				Puzzle.setValue((Puzzle.empty-1),change);
				Puzzle.course = "left";
			}
		}
		if(direction.equals("right")) {									//Jika arah yang diganti adalah "right" atau kanan maka ubin yang kosong akan ditukar dengan sebelah kanannya
			if(Puzzle.empty != 3 && Puzzle.empty  != 7 && Puzzle.empty  != 11 && Puzzle.empty  != 15) {
				Puzzle.setValue(Puzzle.empty, Puzzle.getValue(Puzzle.empty+1));
				Puzzle.setValue((Puzzle.empty+1),change);
				Puzzle.course = "right";
			}
		}
		if(direction.equals("down")) {									//Jika arah yang diganti adalah "down" atau bawah maka ubin yang kosong akan ditukar dengan sebelah bawahnya
			if(Puzzle.empty != 12 && Puzzle.empty  != 13 && Puzzle.empty  != 14 && Puzzle.empty  != 15) {
				Puzzle.setValue(Puzzle.empty, Puzzle.getValue(Puzzle.empty+4));
				Puzzle.setValue((Puzzle.empty+4),change);
				Puzzle.course = "down";
			}
		}
		if(direction.equals("up")) {									//Jika arah yang diganti adalah "up" atau atas maka ubin yang kosong akan ditukar dengan sebelah atasnya
			if(Puzzle.empty != 0 && Puzzle.empty  != 1 && Puzzle.empty  != 2 && Puzzle.empty  != 3) {
				Puzzle.setValue(Puzzle.empty, Puzzle.getValue(Puzzle.empty-4));
				Puzzle.setValue((Puzzle.empty-4),change);
				Puzzle.course = "up";
			}
		}
		Puzzle.cost = Puzzle.getCost();									//Memanggil fungsi getCost untuk mencari nilai cost dari puzzle yang telah ditukar
		Puzzle.empty = Puzzle.getEmpty();								//Memanggil fungsi getEmpty untuk mencari indeks dengan ubin kosong
	}
	public void addStack(puzzle Puzzle,Stack<puzzle> step) {			//Fungsi addStack untuk memasukkan langkah-langkah puzzle kedalam stack
		while(Puzzle != null) {											//Langkah-langkah puzzle dimasukkan dengan menggunakan linked list dari anak puzzle hingga ke akar
			step.add(Puzzle);
			Puzzle = Puzzle.parent;
		}
	}
	
	public int solvePuzzle(puzzle Puzzle, Stack<puzzle> step) {			//Fungsi solvePuzzle untuk menyelesaikan puzzle
		Queue<puzzle>queue = new PriorityQueue<puzzle>();				//Membuat objek queue dengan class PriorityQueue
		int bangkit = 1;												//Membuat variabel bangkit dengan nilai 1 untuk menghitung simpul yang dibangkitkan
		queue.add(Puzzle);												//Memasukkan puzzle akar ke dalam queue
		boolean solve = false;											//Membuat boolean solve untuk mengecek apakah puzzle sudah selesai
		while (!solve) {												//Selama solve belum bernilai true
			puzzle now = queue.poll();									//Ambil puzzle dari queue yang paling depan (puzzle dengan cost terkecil)
			if(now.solved() || bangkit >5000000) {						//Jika puzzle sudah selesai atau simpul yang dibangkitkan sudah lebih dari 5 juta
				solve = true;											//Jadikan solve menjadi true dan panggil simpul addStack
				addStack(now,step);
				Puzzle = now;
				return bangkit;											//kembalikan nilai bangkit
			}															//Jika belum
			puzzle right = new puzzle(now);								//Buat keempat simpul baru dengan arah puzzle kanan, kiri, bawah atas
			puzzle left = new puzzle(now);
			puzzle down = new puzzle(now);
			puzzle up = new puzzle(now);
			swap(left,"left");
			swap(right,"right");
			swap(down,"down");
			swap(up,"up");
			if(left.getEmpty() != now.getEmpty() && !now.course.equals("right")) {	//Jika simpul baru berhasil diswap dan simpul parent tidak habis ditukar dari kanan
				queue.add(left);													//masukkan ke dalam queue;
				bangkit++;
			}
			if(right.getEmpty() != now.getEmpty()&& !now.course.equals("left")) {	//Jika simpul baru berhasil diswap dan simpul parent tidak habis ditukar dari kiri
				queue.add(right);													//masukkan ke dalam queue;
				bangkit++;
			}
			if(down.getEmpty() != now.getEmpty()&& !now.course.equals("up")) {	//Jika simpul baru berhasil diswap dan simpul parent tidak habis ditukar dari atas
				queue.add(down);													//masukkan ke dalam queue;
				bangkit++;
			}
			if(up.getEmpty() != now.getEmpty()&& !now.course.equals("down")) {	//Jika simpul baru berhasil diswap dan simpul parent tidak habis ditukar dari bawah
				queue.add(up);													//masukkan ke dalam queue;
				bangkit++;
			}
			
		}
		return bangkit;															//kembalikan nilai bangkit
		
	}
}
