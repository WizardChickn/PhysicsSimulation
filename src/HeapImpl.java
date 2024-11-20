class HeapImpl<T extends Comparable<? super T>> implements Heap<T> {
	private static final int INITIAL_CAPACITY = 128;
	private T[] _storage;
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
		// TODO: implement me
		_storage[_numElements] = data;
		_numElements++;
		trickleUp(_numElements-1);
	}

	/**
	 * Switches two elements stored in the heap
	 * @param x the index of one element
	 * @param y the index of another element
	 */
	public void swap(int x, int y){
		if(x<_numElements&&y<_numElements){
			T t = _storage[x];
			_storage[x]=_storage[y];
			_storage[y] = t;
		}
	}

	public T removeFirst () {
		// TODO: implement me
		T t =_storage[0] ;
		_storage[0] = _storage[_numElements - 1];
		_storage[_numElements - 1]=null;
		_numElements--;
		trickleDown(0);
		return t;
	}

	/**
	 * Moves down a given element to its correct position compared to its neighboring items
	 * @param index the item that is being moved down
	 */
	public void trickleDown (int index) {
		while (index<_numElements-1&&_storage[index].compareTo(_storage[index+1])<0){
			swap(index, index+1);
			index++;
		}
	}

	/**
	 * Moves up a given element to its correct position compared to its neighboring items
	 * @param index the item that is being moved up
	 */
	public void trickleUp (int index) {
		System.out.println(index+" "+_numElements);
		if ((_numElements>1)){
		while (index>0&&_storage[index].compareTo(_storage[index-1])>0){
			System.out.println(index);
			swap(index, index-1);
			index--;
		}}
	}

	public int size () {
		return _numElements;
	}
}
