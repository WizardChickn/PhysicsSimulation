import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.util.*;

public class HeapTester {
	private void permute (int[] array) {
		final Random random = new Random();
		for (int i = array.length - 1; i >= 0; i--) {
			final int j = random.nextInt(i+1);
			final int temp = array[i];
			array[i] = array[j];
			array[j] = temp;
		}
	}

	@Test
	public void testShuffled () {
		final int N = 1000;
		final int[] numbers = new int[N];
		for (int i = 0; i < N; i++) {
			numbers[i] = i;
		}
		permute(numbers);

		final HeapImpl<Integer> heap = new HeapImpl<Integer>();
		for (int i = 0; i < N; i++) {
			heap.add(numbers[i]);
		}
		
		heap.printList();
		assertEquals(N, heap.size());
		for (int i = N-1; i >= 0; i--) {
			
			System.out.println(heap.removeFirst());
			
			//assertEquals((Integer) i, heap.removeFirst());
		}
		assertEquals(0, heap.size());
	}

	@Test
	public void testRemoveFirst (){
		Integer[] exampleList = {4,3,2,1};
		final HeapImpl<Integer> heap = new HeapImpl<Integer>(exampleList , 4);
		
		assertEquals(4, heap.removeFirst());
		assertEquals(3, heap.removeFirst());
		assertEquals(2, heap.removeFirst());
		assertEquals(1, heap.removeFirst());
	}

	@Test
	public void testSwap (){
		Integer[] exampleList = {1,2,3,4};
		final HeapImpl<Integer> heap = new HeapImpl<Integer>(exampleList , 4);
		Integer[] outputOne = {2,1,3,4};
		heap.swap(0,1);
		assertEquals(outputOne[0], heap.removeFirst());
		
	}
	@Test
	public void testTrickleDown (){
		Integer[] exampleList = {2,1,3,4};
		final HeapImpl<Integer> heap = new HeapImpl<Integer>(exampleList , 4);
		Integer[] outputOne = {2,4,3,1};
		heap.trickleDown(1);
		for (int i = 0; i <= 3; i++) {
			assertEquals(outputOne[i], heap.removeFirst());
		}
	}

	@Test
	public void testBubbleUp (){
		Integer[] exampleList = {2,1,3,4};
		Integer[] outputOne = {4,2,3,1};
		final HeapImpl<Integer> heap = new HeapImpl<Integer>(exampleList , 4);
		final HeapImpl<Integer> resultHeap = new HeapImpl<Integer>(outputOne , 4);
		heap.bubbleUp(3);
		for (int i = 0; i <= 3; i++) {
			assertEquals(resultHeap.removeFirst(), heap.removeFirst()); //4231 -> 321 -> 21 -> 1
		}
	}
}
