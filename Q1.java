import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Q1 {
    public static void main(String[] args) throws InterruptedException {
        int numOfPresents = 500000;
        int numOfServants = 4;

        PresentChain presentChain = new PresentChain();
        int[][] servantPresentTags = new int[numOfServants][numOfPresents / numOfServants];

        for (int i = 0; i < numOfPresents; i++) {
            servantPresentTags[i % numOfServants][i / numOfServants] = i + 1;
        }

        ServantThread[] servantThreads = new ServantThread[numOfServants];
        for (int i = 0; i < numOfServants; i++) {
            servantThreads[i] = new ServantThread(presentChain, servantPresentTags[i]);
            servantThreads[i].start();
        }
        for (ServantThread servantThread : servantThreads) {
            servantThread.join();
        }

        for (int i = 1; i <= numOfPresents; i++) {
            if (presentChain.contains(i)) {
                System.out.println("Present with tag " + i + " is still in the chain");
            }
        }
        int totalThankYouNotes = 0;
        for (ServantThread servantThread : servantThreads) {
            servantThread.join();
            totalThankYouNotes += servantThread.getThankYouNotesWritten();
        }

    System.out.println("Total \"Thank you\" notes written: " + totalThankYouNotes);
    }
}

class Present {
    int tag;
    Present next;

    Present(int tag) {
        this.tag = tag;
        this.next = null;
    }
}

class ServantThread extends Thread {
    private final PresentChain presentChain;
    private final int[] presentTags;
    private int thankYouNotesWritten;

    ServantThread(PresentChain presentChain, int[] presentTags) {
        this.presentChain = presentChain;
        this.presentTags = presentTags;
        this.thankYouNotesWritten = 0;
    }

    @Override
    public void run() {
        for (int tag : presentTags) {
            // Add a present to the chain
            presentChain.addPresent(tag);

            // Write a "Thank you" note and remove the present from the chain
            if (presentChain.removePresent(tag)) {
                thankYouNotesWritten++;
            }
        }
    }

    public int getThankYouNotesWritten() {
        return thankYouNotesWritten;
    }
}


class PresentChain {
    private final ReentrantLock lock = new ReentrantLock();
    private Present head;

    public void addPresent(int tag) {
        lock.lock();
        try {
            Present newPresent = new Present(tag);
            if (head == null) {
                head = newPresent;
            } else {
                Present current = head;
                Present previous = null;
                while (current != null && current.tag < tag) {
                    previous = current;
                    current = current.next;
                }
                if (previous == null) {
                    newPresent.next = head;
                    head = newPresent;
                } else {
                    previous.next = newPresent;
                    newPresent.next = current;
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public boolean removePresent(int tag) {
        lock.lock();
        try {
            if (head == null) {
                return false;
            }
            if (head.tag == tag) {
                head = head.next;
                return true;
            }
            Present current = head;
            Present previous = null;
            while (current != null && current.tag != tag) {
                previous = current;
                current = current.next;
            }
            if (current == null) {
                return false;
            }
            previous.next = current.next;
            return true;
        } finally {
            lock.unlock();
        }
    }

    public boolean contains(int tag) {
        lock.lock();
        try {
            Present current = head;
            while (current != null) {
                if (current.tag == tag) {
                    return true;
                }
                current = current.next;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }
}