package edu.smith.cs.csc212.adtr.real;

import edu.smith.cs.csc212.adtr.ListADT;
import edu.smith.cs.csc212.adtr.errors.BadIndexError;
import edu.smith.cs.csc212.adtr.errors.EmptyListError;
import edu.smith.cs.csc212.adtr.errors.TODOErr;

/**
 * This is a data structure that has an array inside each node of an ArrayList.
 * Therefore, we only make new nodes when they are full. Some remove operations
 * may be easier if you allow "chunks" to be partially filled.
 * 
 * @author jfoley
 * @param <T> - the type of item stored in the list.
 */
public class ChunkyArrayList<T> extends ListADT<T> {
	private int chunkSize;
	private GrowableList<FixedSizeList<T>> chunks;
	/*
	 * Keeping a pointer of which chunk i am at used for setIndex() and removeIndex()
	 */
	public int numChunk;
	public int rNumChunk;

	public ChunkyArrayList(int chunkSize) {
		this.chunkSize = chunkSize;
		chunks = new GrowableList<>();
		this.numChunk = 0;
		this.rNumChunk = 0;
	}
	
	private FixedSizeList<T> makeChunk() {
		return new FixedSizeList<>(chunkSize);
	}

	@Override
	public T removeFront() {
		// complete (?)
		if(this.chunks.isEmpty()) {
			throw new EmptyListError();
		}
		
		FixedSizeList<T> firstChunk = this.chunks.getFront();
		T removedValue = firstChunk.getFront();
		
		if(this.chunks.size() == 1) {
			return firstChunk.removeFront();
		} else {
			for(int i = 0; i < this.chunks.size() - 1; i++) {
				FixedSizeList<T> current = this.chunks.getIndex(i);
				FixedSizeList<T> nextChunk = this.chunks.getIndex(i+1);
				current.removeFront();
				current.addBack(nextChunk.getFront());
				
				if(i+1 == this.chunks.size() -1) {
					nextChunk.removeFront();
				}
				
				if(chunks.getBack().size() == 0) {
					chunks.removeBack();
				}
			}
		}
		
		return removedValue;
	}

	@Override
	public T removeBack() {
		if(this.chunks.isEmpty()) {
			throw new EmptyListError();
		}
		
		FixedSizeList<T> lastChunk = this.chunks.getBack();
		T removedValue = lastChunk.getBack();
		
		if(lastChunk.size() == 1) {
			lastChunk.removeBack();
			this.chunks.removeBack();
		} else {
			lastChunk.removeBack();
		}
		return removedValue;
	}

	@Override
	public T removeIndex(int index) {
		checkNotEmpty();
		checkInclusiveIndex(index);
		
		int start = 0;
		for (FixedSizeList<T> chunk : this.chunks) {
			// calculate bounds of this chunk.
			int end = start + chunk.size();
			
			// Check whether the index should be in this chunk:
			if (start <= index && index < end) {
				return chunk.removeIndex(index - start);
			}
			
			// update bounds of next chunk.
			start = end;
		}
		throw new BadIndexError(index);
	}

	@Override
	public void addFront(T item) {
		if (this.chunks.isEmpty()) {
			FixedSizeList<T> newFront =  makeChunk();
			this.chunks.addFront(newFront);
		}
		
		FixedSizeList<T> front = chunks.getFront();
		if(front.isFull()) {
			front = makeChunk();
			chunks.addFront(front);
		}
		front.addFront(item);
	}

	@Override
	public void addBack(T item) {
		if (this.chunks.isEmpty()) {
			FixedSizeList<T> newFront =  makeChunk();
			this.chunks.addFront(newFront);
		}
		
		FixedSizeList<T> back = chunks.getBack();
		if(back.isFull()) {
			back = makeChunk();
			chunks.addBack(back);
		}
		back.addBack(item);
	}

	@Override
	public void addIndex(int index, T item) {
		// THIS IS THE HARDEST METHOD IN CHUNKY-ARRAY-LIST.
		// DO IT LAST.
		
		int chunkIndex = 0;
		int start = 0;
		
		if (index == 0) {
			addFront(item);
			return;	
		}
		
		if (index == this.size()) {
			
		}
		
		for (FixedSizeList<T> chunk : this.chunks) {
			// calculate bounds of this chunk.
			int end = start + chunk.size();
			
			// Check whether the index should be in this chunk:
			if (start <= index && index <= end) {
				if (chunk.isFull()) {
					// check can roll to next
					// or need a new chunk
					
					// COMEEEEE HERE U IDIOT
		
				} else {
					// put right in this chunk, there's space.
					throw new TODOErr();
				}	
				// upon adding, return.
				// return;
			}
			
			// update bounds of next chunk.
			start = end;
			chunkIndex++;
		}
		throw new BadIndexError(index);
	}
	
	@Override
	public T getFront() {
		return this.chunks.getFront().getFront();
	}

	@Override
	public T getBack() {
		return this.chunks.getBack().getBack();
	}


	@Override
	public T getIndex(int index) {
		if (this.isEmpty()) {
			throw new EmptyListError();
		}
		int start = 0;
		for (FixedSizeList<T> chunk : this.chunks) {
			// calculate bounds of this chunk.
			int end = start + chunk.size();
			
			// Check whether the index should be in this chunk:
			if (start <= index && index < end) {
				return chunk.getIndex(index - start);
			}
			
			// update bounds of next chunk.
			start = end;
		}
		throw new BadIndexError(index);
	}
	
	@Override
	public void setIndex(int index, T value) {
		// tried using recursion here - does it work??
		checkNotEmpty();
		checkInclusiveIndex(index);
		FixedSizeList<T> current = this.chunks.getIndex(this.numChunk);
		
		if(current.size() > index) {
			current.setIndex(index, value);
			this.numChunk = 0;
		} else {
			this.numChunk += 1; 
			index = index - current.size();
			setIndex(index, value);
		}
		
	}

	@Override
	public int size() {
		int total = 0;
		for (FixedSizeList<T> chunk : this.chunks) {
			total += chunk.size();
		}
		return total;
	}

	@Override
	public boolean isEmpty() {
		return this.chunks.isEmpty();
	}
	
}