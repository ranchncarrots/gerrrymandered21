
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;


public class Algos {
    /**
     * Central algo to Gerrymander congressional Districts
     * @param graph A graph of Precints in a specific state
     * @param currentSplit The current split deployed by the recursive backtracking algo
     * @param currentProcess The current vertices in the queue to be processed
     * @return An optimal split
     */
    public static Split partitioner(Graph graph, Split currentSplit, Queue<Vertex> currentProcess) {
        //System.out.println("Lets get this show on the road");
        /**
         * There are TWO Base Cases
         * 1. We are out of queues to process however we do not have a fully functional Split
         * 2. We have a valid split and can return
         */

        /**
         * Pop off the Vertex to be processed
         */

        if (currentProcess.isEmpty() && currentSplit.totalAddedTracker != graph.vertices.size()) {
            //System.out.println(currentProcess);
            //System.out.println(currentSplit);
            return null;
        } else if (currentProcess.isEmpty()) {
            //System.out.println("hereeee");
            //System.out.println(currentSplit);
            return currentSplit;
        }

        Vertex j = currentProcess.peek();
        currentProcess.remove();
        //System.out.println("Working on " + j);

        /**
         * get all adjacent vertices
         */
        List<Vertex> adjacentsOriginal = graph.getAdjacentVertices(j);
        /**
         * copy over valid vertices to that set (they havent already been processed)
         */
        List<Vertex> adjacents = new ArrayList<Vertex>();
        for (int i = 0; i < adjacentsOriginal.size(); i++) {
            if ((currentSplit.seenSoFar.get(adjacentsOriginal.get(i)) == null)
                    && (currentSplit.attemptedThisPartition.get(adjacentsOriginal.get(i)) == null) && (currentSplit.seenThisPartition.get(adjacentsOriginal.get(i)) == null)) {
                adjacents.add(adjacentsOriginal.get(i));
            }
        }


        //        public int districtLimit;
//        public List<ArrayList<Vertex>> districts;
//        public Map<Vertex, Integer> seenSoFar;
//        public int currentPosition;
//        ArrayList<Split.SplitData> trackerAtIndex;
//        int copyDistrictLimit = currentSplit.districtLimit;
//        List<ArrayList<Vertex>> copyDistricts = new ArrayList<ArrayList<Vertex>>(currentSplit.districts);
//        Map<Vertex, Integer> copySeenSoFar = new HashMap<Vertex, Integer>(currentSplit.districtLimit);
//        int copyCurrentPosition = currentSplit.currentPosition;
//        ArrayList<Split.SplitData> copyTrackerAtIndex = new ArrayList<Split.SplitData>(currentSplit.trackerAtIndex);
//        Map<Vertex, Integer> copyAttemptedThisPartition = new HashMap<Vertex, Integer>(currentSplit.attemptedThisPartition);
//
//
//        int copyDistrictLimit2 = currentSplit.districtLimit;
//        List<ArrayList<Vertex>> copyDistricts2 = new ArrayList<ArrayList<Vertex>>(currentSplit.districts);
//        Map<Vertex, Integer> copySeenSoFar2 = new HashMap<Vertex, Integer>(currentSplit.districtLimit);
//        int copyCurrentPosition2 = currentSplit.currentPosition;
//        ArrayList<Split.SplitData> copyTrackerAtIndex2 = new ArrayList<Split.SplitData>(currentSplit.trackerAtIndex);
//        Map<Vertex, Integer> copyAttemptedThisPartition2 = new HashMap<Vertex, Integer>(currentSplit.attemptedThisPartition);
//        copyAttemptedThisPartition.put(j, 0);
//        copyAttemptedThisPartition2.put(j, 0);
        Queue<Vertex> copyCurrentProcess = new ArrayBlockingQueue<Vertex>(10000);
        Queue<Vertex> copyCurrentProcess2 = new ArrayBlockingQueue<Vertex>(10000);
        
        Vertex[] queueToArray = currentProcess.toArray(new Vertex[currentProcess.size()]);
        copyCurrentProcess.addAll(currentProcess);
        copyCurrentProcess2.addAll(currentProcess);
        copyCurrentProcess2.addAll(adjacents);
        Split originalSplit = new Split(currentSplit);
        Split newSplit = new Split(currentSplit);
        // make the current vertex partition as attempted
        originalSplit.attemptedThisPartition.put(j, 0);
        newSplit.attemptedThisPartition.put(j, 0);
        //make all neighbours as has been seen for newSplit
        for (int i = 0; i < adjacents.size(); i++) {
            newSplit.seenThisPartition.put(adjacents.get(i), 0);
        }

        Vertex[] bruh = new Vertex[1];
        bruh[0] = j;
        newSplit.add(graph, bruh);
        adjacents.clear();
        for (int i = 0; i < adjacentsOriginal.size(); i++) {
            if ((newSplit.seenSoFar.get(adjacentsOriginal.get(i)) == null)
                    && (newSplit.attemptedThisPartition.get(adjacentsOriginal.get(i)) == null) && (newSplit.seenThisPartition.get(adjacentsOriginal.get(i)) == null)) {
                adjacents.add(adjacentsOriginal.get(i));
            }
        }
        //copyCurrentProcess.addAll(adjacents);
        List<Vertex> testingThing = new ArrayList<Vertex>();
        for (int i = 0; i < adjacentsOriginal.size(); i++) {
            if ((newSplit.seenSoFar.get(adjacentsOriginal.get(i)) == null)
                    && (newSplit.attemptedThisPartition.get(adjacentsOriginal.get(i)) == null)) {
                testingThing.add(adjacentsOriginal.get(i));
            }
        }
        copyCurrentProcess2.addAll(adjacents);
//        System.out.println(j);
        //System.out.println(originalSplit);
        //System.out.println(newSplit);
        //System.out.println(copyCurrentProcess2);

//        if (currentSplit.totalAddedTracker != graph.vertices.size() && currentProcess.isEmpty()) {
//            System.out.println("this null");
//            return null;
//        } else if (adjacents.isEmpty() && currentProcess.isEmpty()) {
//            return currentSplit;
//        }

        Split withoutCurrent = null;
        if (testingThing.size() >= 10) {
            withoutCurrent = partitioner(graph, originalSplit, copyCurrentProcess);
        }

        Split withCurrent = partitioner(graph, newSplit, copyCurrentProcess2);
        List<Split> f = new ArrayList<Split>();
            f.add(withoutCurrent);

        f.add(withCurrent);
        //System.out.println(newSplit.districts);
        //calculate the valid power set

        return DistrictMax(f);

    }

    public static Split DistrictMax(List<Split> passedIn) {
        //System.out.println("its someting");
    Split currentMax = null;
    int maxAt = 0;
    for (int i = 0; i < passedIn.size(); i++) {
        Split temp = passedIn.get(i);
        int currentCounter = 0;
        if (temp == null) {
            continue;
        }
        for (int j = 0; j < temp.trackerAtIndex.size(); j++) {
            int clintonVotes = temp.trackerAtIndex.get(j).clinton;
            int trumpVotes = temp.trackerAtIndex.get(j).trump;
            if (clintonVotes - trumpVotes > 0) {
                currentCounter++;
            }
        }
        if (currentCounter > maxAt) {
            maxAt = currentCounter;
            currentMax = temp;

        }
    }

    return currentMax;
    }

    public static boolean isConnected(Graph graph) {
        Queue<Vertex> queue = new ArrayBlockingQueue(50000);
        queue.add(graph.vertices.get(0));
        Map<Vertex, Integer> visitTracker = new HashMap<Vertex, Integer>();
        visitTracker.put(graph.vertices.get(0), 1);
        while (!queue.isEmpty()) {
            Vertex j = queue.peek();
            queue.remove();
            System.out.println(j);
            List<Vertex> adjacents = graph.getAdjacentVertices(j);
            if (j.equals(new Vertex("BELVIDERE 6", 0, 0))) {
                System.out.println(adjacents);
            }
            for (int i = 0; i < adjacents.size(); i++) {

                if (visitTracker.containsKey(adjacents.get(i)) == false) {
                    visitTracker.put(adjacents.get(i), 1);
                    queue.add(adjacents.get(i));
                }
            }

        }
        if (visitTracker.size() == graph.vertices.size()) {
            return true;
        }
        System.out.println(visitTracker.size());
        List<Vertex> newVertex = new ArrayList<>();
        for (int i = 0; i < graph.vertices.size(); i++) {
            newVertex.add(new Vertex(graph.vertices.get(i).name, graph.vertices.get(i).clinton, graph.vertices.get(i).trump));
        }
        for (Map.Entry mapElement : visitTracker.entrySet()) {
            Vertex key = (Vertex) mapElement.getKey();
            for (int i = 0; i < newVertex.size(); i++) {
                if (newVertex.get(i).equals(key)) {
                    newVertex.remove(i);
                    break;
                }
            }

        }

        System.out.println(newVertex);

        return false;

    }
}