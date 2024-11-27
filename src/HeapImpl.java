class HeapImpl<T extends Comparable<? super T>> implements Heap<T> {
	private static final int INITIAL_CAPACITY = 128;
	public T[] _storage; //TODO make private
	private int _numElements;

	@SuppressWarnings("unchecked")
	public HeapImpl () {
		_storage = (T[]) new Comparable[INITIAL_CAPACITY];
		_numElements = 0;
	}

	public HeapImpl (T[] storage, int numElements) {
		_storage = storage;
		_numElements = numElements;
	}

	@SuppressWarnings("unchecked")
	public void add (T data) {
		if (!(_numElements < _storage.length)) { // checking to see if the current _storage is filled 
			T[] storage = (T[]) new Comparable[2 * _storage.length]; // creating a array doubled in size to continue adding data
			System.arraycopy(_storage, 0, storage, 0, _storage.length);
			_storage = storage;
		}

		_storage[_numElements] = data;
		_numElements++;
		bubbleUp(_numElements - 1);
	}

	/**
	 * Switches two elements stored in the heap
	 * @param x the index of one element
	 * @param y the index of another element
	 */
	public void swap(int x, int y){
		if(x < _numElements && y < _numElements){
			T t = _storage[x];
			_storage[x] = _storage[y];
			_storage[y] = t;
		}
	}

	public T removeFirst () {
		T t =_storage[0] ;
		_storage[0] = _storage[_numElements - 1];
		_storage[_numElements - 1] = null;
		trickleDown(0);
		_numElements--;
		return t;
	}

	/**
	 * Moves down a given element to its correct position compared to its child nodes
	 * @param index the item that is being moved down
	 */
	public void trickleDown (int index) {
		int swapIndex  = 0;
		while ((((index * 2 + 1) < _numElements-1 && _storage[index].compareTo(_storage[index * 2 + 1]) < 0) || ((index * 2 + 2) < _numElements-1 && _storage[index].compareTo(_storage[index*2+2]) < 0))){
			if ((index * 2 + 2) < _numElements-1 && _storage[index * 2 + 2].compareTo(_storage[index * 2 + 1]) > 0){
				swapIndex = index * 2 + 2;
			}
			else {
			
				swapIndex = index * 2 + 1;
			}
			swap(index, swapIndex);
			index = swapIndex;
		}
	}

	/**
	 * Moves up a given element to its correct position compared to its parent nodes
	 * @param index the item that is being moved up
	 */
	public void bubbleUp (int index) throws RuntimeException{
		if ((_numElements >= 1)) {
			//System.out.println("start "+index);
			while (index > 0 && (_storage[index].compareTo(_storage[(index - 1) / 2]) >= 0)){
				if (_storage[index].compareTo(_storage[(index - 1) / 2]) == 0){ //TODO watch this
					//throw new RuntimeException();
				} else
				swap(index, (index - 1) / 2);
				index = (index - 1) / 2;
			}
			//System.out.println("end "+index);
		}
	}

	/**
	 * prints _storage divided into the levels of nodes in the heap
	 */
	public void printList(){
		int vals = 6;
		System.out.println(_storage[0] + " ");
		System.out.print(_storage[1] + " ");
		System.out.println(_storage[2] + " ");
		for (int i = 3; i < _numElements; i++){
			if (i == vals){
			vals *= 2;
			vals += 2;
			System.out.println(_storage[i] + " ");
			}
			else{
				System.out.print(_storage[i] + " ");
			}
		}
	}
	
	public int size () {
		return _numElements;
	}

}
