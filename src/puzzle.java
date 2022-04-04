import java.awt.Component;
import java.awt.HeadlessException;
import java.util.concurrent.ThreadLocalRandom;
import java.lang.*;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
public class puzzle implements Comparable<puzzle>{				//Membuat kelas puzzle untuk mempermudah perhitungan puzzle
	private int[] board;										//Atribut board yang terdiri dari array of integer untuk menyimpan puzzle
	public int cost;											//Atribut cost untuk menghitung cost setiap simpul
	public int empty;											//Atribut empty untuk menyimpan indeks yang berisi ubin kosong
	public int goal;											//Atribut goal untuk menghitung goal puzzle yang akan diselesaikan
	public int depth;											//Atribut depth untuk menyimpan jarak simpul dari akar
	public String course;										//Atribut String course untuk menyimpan arah hasil perubahan puzzle
	public puzzle parent;										//Atribut parent untuk menyimpan alamat dari simpul parent puzzle tersebut
	public puzzle() {											//constructor puzzle tanpa input
		this.board = new int[16];
		this.cost = 0;
		this.empty = 0;
		this.goal = 0;
		this.course = "none";
		this.depth = 0;
		this.parent = null;
	}
	public puzzle(puzzle Puzzle) {								//constructor puzzle dengan input Puzzle untuk membuat puzzle baru dari Puzzle
		this.board = new int[16];
		for(int x=0;x<16;x++) {
			this.board[x] = Puzzle.board[x];
		}
		this.cost = Puzzle.cost;
		this.empty = this.getEmpty();
		this.goal = Puzzle.goal;
		this.course = new String(Puzzle.course);
		this.depth = Puzzle.depth;
		this.parent = Puzzle;
	}
	
	public int getEmpty() {										//method untuk mencari indeks yang berisi ubin kosong
		for (int x=0; x<16;x++) {
			if(this.board[x] == 16) {
				this.empty = x;
				return x;
			}
		}
		return -1;
	}
	public int getCost() {										//method untuk mencari cost
		this.depth++;
		this.cost = this.depth+1;								//cost sebuah simpul adalah jarak antara simpul ke akar 
		for(int x=0; x<16;x++) {
			if(this.board[x] == 16) continue;
			else if(this.board[x] != (x+1)) {
				this.cost++;		//ditambah dengan jarak setiap angka dari 1-15  antara angka tersebut dengan posisi seharusnya
			}
		}
		return this.cost;										//mengembalikan nilai cost
	}

	public int getIndex(int value) {							//method untuk mencari indeks di atribut board yang sesuai dengan nilai value
		for(int x=0;x<16;x++) {
			if(this.board[x] == value)return x;
		}
		return -1;
	}
	public int getGoal() {										//method untuk mendapatkan nilai goal
		this.goal = 0;
		if(this.getEmpty()== 1||  this.getEmpty()== 3 || this.getEmpty()== 4|| this.getEmpty()== 6 || this.getEmpty()== 9 || this.getEmpty()== 11 || this.getEmpty()== 12 ||this.getEmpty()== 14 ) {
			this.goal++;										//Jika ubin kosong berada di posisi 2,4,5,7,10,12,13,15 maka X bernilai satu
		}
		System.out.printf("Nilai X = %d\n",this.goal);			//Mencetak nilai X
		System.out.println("KURANG(1) = 0");					//Mencetak fungsi KURANG(1) yang selalu bernilai 0
		for(int x=2;x<=16;x++) {								//Iterasi dari 2 hingga 16
			int check = this.getIndex(x);
			int before = this.goal;
			for(int i = check+1 ; i<16;i++) {					//Iterasi lagi untuk mencari nilai KURANG(i)
				if(this.board[i]<x)this.goal++;
			}
			System.out.printf("KURANG(%d) = %d\n",x,(this.goal - before));		//Mencetak nilai fungsi KURANG(i)
		}
		return this.goal;
	}
	public boolean solved() {									//Method untuk mengecek apakah Puzzle sudah selesai apa belum
		for(int x = 0;x<15;x++) {
			if(this.board[x] != x+1)return false;				//Puzzle dianggap sudah selesai jika semua angka dalam puzzle sudah sesuai dengan posisinya
		}
		return (this.board[15] == 16);
	}
	public void acak() {										//Method untuk mengacak sebuah puzzle
		boolean[] cek = {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false};	//array cek dengan tipe boolean untuk mengecek apakah puzzle dengan suatu indeks sudah terisi atau belum
		for(int x=0;x<16;x++) {
			int num;
			if(x>=0 && x<8) num = ThreadLocalRandom.current().nextInt(1,8); // Jika x antara 0 hingga 7 maka akan mengacak integer dari 1 hingga 8
			else num = ThreadLocalRandom.current().nextInt(9,16);			// Jika x antara 8 hingga 15 maka akan mengacak integer dari 9 hingga 16
			while(cek[num-1] == true) {										//Jika angka yang diacak sudah ada sebelumnya maka akan ditambah satu hingga angka tersebut belum ada
				num++;
				if(num>16)num=1;											//Jika angka melewati 8 maka akan dijadikan 1
			}
			this.board[x] = num;											//Masukkan angka yang telah diacak ke indeks x
			cek[num-1] = true;												//Jadikan array cek dengan indeks num-1 menjadi true untuk memberi tahu bahwa angka tersebut sudah ada	
		}
	}
	public int getValue(int idx) {							//Method untuk mendapatkan value dengan indeks idx di array board
		return this.board[idx];
	}
	public void setValue(int idx, int value) {				//Method untuk mengeset value dengan indeks idx di array board
		this.board[idx] = value;
	}
	public void printPuzzle() {								//Method untuk mengeprint isi puzzle
		for(int x=0;x<16;x++) {
			if(this.board[x] == 16)System.out.printf("* ");	//Ubin yang kosong atau di board dilambangkan dengan angka 16 akan dicetak dengan karakter "*"
			else 
				System.out.printf("%d ",this.board[x]);		//Selain itu cetak angka dari isi board[x]
			if((x+1) %4 == 0) System.out.println();
		}
	}
	public int compareTo(puzzle p) {						//Method compareTo yang merupakan implementasi dari comparable
		if(this.cost > p.cost)return 1;						//Jika cost puzzle saat itu lebih besar dari p kembalikan 1
		else if(this.cost<p.cost)return -1;					//Jika cost puzzle saat itu lebih kecil dari p kembalikan -1
		else {												//Jika cost puzzle bernilai sama
			if(this.depth<p.depth) return 1;				//Jika dept puzzle lebih kecil dari p kembalikan 1
			else if(this.depth>p.depth) return -1;			//Jika dept puzzle lebih besar dari p kembalikan -1
			else											//Jika sama maka kembalikan 0
			return 0;
		}
	}
}
class SimpanPilihFile extends JFileChooser {			//Fungsi untuk memunculkan ui dari pilih file
    protected JDialog createDialog(Component parent) throws HeadlessException {
        JDialog dialog = super.createDialog(parent);
        dialog.setAlwaysOnTop(true);
        return dialog;
    }
}