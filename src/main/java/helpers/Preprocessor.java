package helpers;

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;

public class Preprocessor {

    public static PreprocessResult preprocessCiphertext(int[] raw) {

        Int2IntOpenHashMap map = new Int2IntOpenHashMap();
        map.defaultReturnValue(-1);

        int next = 0;
        for (int sym : raw) {
            if (map.get(sym) == -1) {
                map.put(sym, next++);
            }
        }

        int[] ctIdx = new int[raw.length];
        for (int i = 0; i < raw.length; i++) {
            ctIdx[i] = map.get(raw[i]);
        }

        IntArrayList[] positions = new IntArrayList[next];
        for (int i = 0; i < next; i++) {
            positions[i] = new IntArrayList();
        }

        for (int i = 0; i < ctIdx.length; i++) {
            positions[ctIdx[i]].add(i);
        }

        return new PreprocessResult(ctIdx, positions, next);
    }
}
