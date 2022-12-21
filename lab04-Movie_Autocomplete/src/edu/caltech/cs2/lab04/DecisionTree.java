
package edu.caltech.cs2.lab04;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DecisionTree {
    private final DecisionTreeNode root;

    public DecisionTree(DecisionTreeNode root) {
        this.root = root;
    }

    public String predict(Dataset.Datapoint point) {
        OutcomeNode conc = (OutcomeNode) nextNode(point, (AttributeNode) this.root);
        return conc.outcome;
    }

    public DecisionTreeNode nextNode(Dataset.Datapoint point, AttributeNode child) {
        DecisionTreeNode kid = child.children.get(point.attributes.get(child.attribute));
        DecisionTree tree = new DecisionTree(kid);
        return kid.isLeaf() ? tree.root : nextNode(point, (AttributeNode) kid);
    }

    public static DecisionTree id3(Dataset dataset, List<String> attributes) {
        return new DecisionTree(id3helper(dataset, attributes));
    }

    public static DecisionTreeNode id3helper(Dataset dataset, List<String> attributes) {
        if (!dataset.pointsHaveSameOutcome().isEmpty()) {
            return new OutcomeNode(dataset.pointsHaveSameOutcome());
        }
        if (attributes.isEmpty()){
            return new OutcomeNode(dataset.getMostCommonOutcome());
        }
        String a = dataset.getAttributeWithMinEntropy(attributes);
        List<String> features = dataset.getFeaturesForAttribute(a);
        Map<String, DecisionTreeNode> kids = new HashMap<>();
        DecisionTreeNode child;
        for (String f: features){
            Dataset point = dataset.getPointsWithFeature(f);
            if (point.isEmpty()){
                child = new OutcomeNode(dataset.getMostCommonOutcome());
                kids.put(f, child);
            }else {
                List<String>  newa = new ArrayList<>();
                for (String atribute: attributes){
                    if(!atribute.equals(a)){
                        newa.add(atribute);
                    }
                }
                child = id3helper(point, newa);
                kids.put(f, child);
            }
        }
        return new AttributeNode(a, kids);
    }
}