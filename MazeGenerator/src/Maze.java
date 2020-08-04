import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class Maze {
    /* Rows*/
	private int mazeRows;
	/*Columns*/
	private int mazeColumns;
	
    private boolean debug;

    private int totalRooms;
	/* Number of nodes visited*/
	private int numOfNodes;
	
	private Room currentBlock;
	/* Keeps track of visited blocks*/
	private Stack<Room> visitedBlock;
	/* flag to notify end of maze has been reached */
	boolean end;
    /*Matrix for the map*/
	private Room maze[][];
	/* Keeps track of visited nodes */
	private int visited[][];
	
	

	private class Room {
		int n, m;

		boolean left, right, up, down, visited;

		private Room(int n, int m) {
			this.n = n;
			this.m = m;
			this.left = true;
			this.right = true; 
			this.up = true; 
			this.down = true; 
			visited = false;
		}

		private Room(int n, int m, int roomCase, boolean wall) {
			this.n = n;
			this.m = m;
			this.left = true; 
			this.right = true;
			this.up = true; 
			this.down = true; 
			visited = true;
			
			switch (roomCase) {
			case 1:
				this.left = wall;
				break;
			case 2:
				this.right = wall;
				break;
			case 3:
				this.up = wall;
				break;
			case 4:
				this.down = wall;
				break;
			}
		}
	}

	/*
	 * Public constructor
	 */
	public Maze(int cols, int rows, boolean debug) {
		this.mazeRows = cols;
		this.mazeColumns = rows;
		this.debug = debug;
		maze = new Room[this.mazeRows][this.mazeColumns];
		visited = new int[this.mazeRows][this.mazeColumns];
		for (int i = 0; i < this.mazeRows; i++) {
			for (int j = 0; j < this.mazeColumns; j++) {
				maze[i][j] = new Room(i, j);
				visited[i][j] = 0;
			}
		}
		visited[0][0] = 1;
		visited[cols - 1][rows - 1] = 1;
		visitedBlock = new Stack<Room>();
		// Start of maze
		maze[0][0] = new Room(0, 0, 3, false);
		totalRooms = this.mazeRows * this.mazeColumns;
		numOfNodes = 1;
		currentBlock = maze[0][0];
		end = false;
		buildMaze(currentBlock, debug);
		if(debug == false) {
			display();
		}
	}

	/* "X" for the walls and "v" for the path*/
	public void display() {		
		
		for(int i = 0; i < mazeRows; i++) {
			topWall(i);
			leftWall(i);
			
			if(i == (mazeRows - 1)) {
				for (int j = 0; j < mazeColumns; j++) {
					if(j == mazeRows - 1) {
						System.out.print("X   ");
					}
					else {
						System.out.print("X X ");
					}
				}
				System.out.println("X\n");
			}
		}
	}
	
	private void topWall(int row) {
		for(int j = 0; j < mazeColumns; j++) {
			if(maze[row][j].up == false) {
				System.out.print("X   ");
			}
			else if(row != 0 && maze[row - 1][j].down == false) {
				System.out.print("X   ");
			}
			else {
				System.out.print("X X ");
			}
		}
		System.out.println("X");
	}
	
	private void leftWall(int row) {
		if(debug == true) {
		for(int j = 0; j < mazeColumns; j++) {
			if(maze[row][j].left == false && j != 0) {
				System.out.print("  v ");
			}
			else if(j != 0 && maze[row][j - 1].right == false) {
				System.out.print("  v ");
			}
			else if (maze[row][j].visited == true){
				System.out.print("X v ");
			}
			else {
				System.out.print("X   ");
			}
		}
		System.out.println("X");
		}
		else {
			for(int j = 0; j < mazeColumns; j++) {
				if(j != 0 && maze[row][j].left == false) {
					if(visited[row][j] == 1) {
						System.out.print("  v ");
					}
					else {
						System.out.print("    ");
					}
				}
				else if(j != 0 && maze[row][j - 1].right == false) {
					if(visited[row][j] == 1) {
						System.out.print("  v ");
					}
					else {
						System.out.print("    ");
					}
				}
				else if(visited[row][j] == 1) {
					System.out.print("X v ");
				}
				else {
					System.out.print("X   ");
				}
			}
			System.out.println("X");
		}
	}
	
	private void buildMaze(Room current, boolean debug) {
		ArrayList<Room> neighbors = new ArrayList<Room>();
		Random rando = new Random();
		int randoNeighbor;
		
		while(numOfNodes < totalRooms) {
			neighbors = findNeighbors(current);
			if(current.n == mazeRows - 1 && current.m == mazeColumns - 1) {
				end = true;
			}
			if(neighbors.size() >= 1) {
				randoNeighbor = rando.nextInt(neighbors.size());
				destroyWalls(neighbors.get(randoNeighbor), current);
				visitedBlock.push(current);
				current = neighbors.get(randoNeighbor);

				if((!end && !current.visited) || (!end && current.visited)) {
					visited[current.n][current.m] = 1;
				}
				else if(end && !current.visited) {
					visited[current.n][current.m] = 0;
				}
				numOfNodes++;
				if(debug == true) {
					display();
				}
				buildMaze(current, debug);
			}
			else if (neighbors.size() == 0) {
				if(!end && current.visited) {
					visited[current.n][current.m] = 1;
				}
				else if((!end) ||end && current.visited) {
					visited[current.n][current.m] = 0;
				}
				if(visitedBlock.isEmpty() == true) {
					return;
				}
				else {
					current = visitedBlock.pop();
					buildMaze(current, debug);
				}
			}
		}
	}
	
	private void destroyWalls(Room neighbor, Room current) {
		int n = neighbor.n;
		int m = neighbor.m;
		
		if(n < current.n) {
			maze[n][m] = new Room(n, m, 4, false);
		}
		else if(n > current.n) {
			maze[n][m] = new Room(n, m, 3, false);
		}
		else if(m < current.m) {
			maze[n][m] = new Room(n, m, 2, false);
		}
		else if(m > current.m){
			maze[n][m] = new Room(n, m, 1, false);
		}
	}
	
	private ArrayList<Room> findNeighbors(Room current) {
		ArrayList<Room> neighbors = new ArrayList<Room>();
		int n = current.n;
		int m = current.m;
		
		if(n == 0 && m == 0) {
			neighbors.add(maze[n+1][m]);
			neighbors.add(maze[n][m+1]);
		}
		else if(m == maze.length - 1 && n == maze.length - 1) {
			neighbors.add(maze[n-1][m]);
			neighbors.add(maze[n][m-1]);
		}
		else if(n == 0 && m == maze.length - 1) {
			neighbors.add(maze[n+1][m]);
			neighbors.add(maze[n][m-1]);
		}
		else if(n == maze.length - 1 && m == 0) {
			neighbors.add(maze[n-1][m]);
			neighbors.add(maze[n][m+1]);
		}
		else if(n == 0) {
			neighbors.add(maze[n+1][m]);
			neighbors.add(maze[n][m-1]);
			neighbors.add(maze[n][m+1]);
		}
		else if(m == 0) {
			neighbors.add(maze[n-1][m]);
			neighbors.add(maze[n+1][m]);
			neighbors.add(maze[n][m+1]);
		}
		else if(n == maze.length - 1) {
			neighbors.add(maze[n-1][m]);
			neighbors.add(maze[n][m-1]);
			neighbors.add(maze[n][m+1]);
		}
		else if(m == maze.length - 1) {
			neighbors.add(maze[n-1][m]);
			neighbors.add(maze[n+1][m]);
			neighbors.add(maze[n][m-1]);
		}
		else {
			neighbors.add(maze[n-1][m]);
			neighbors.add(maze[n+1][m]);
			neighbors.add(maze[n][m-1]);
			neighbors.add(maze[n][m+1]);
		}
		
		for(int i = neighbors.size() - 1; i >= 0; i--) {
			if(neighbors.get(i).visited == true) {
				neighbors.remove(i);
			}
		}
		return neighbors;
	}
	
}