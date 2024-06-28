/*
 * Copyright (c) 2018 by Gerrit Grunwald
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.utils.gui.charts.sunburst.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.utils.gui.charts.sunburst.ChartData;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.shape.Path;

public class TreeNode {

	private ChartData data;
	private TreeNode parent;
	private TreeNode myRoot;
	private TreeNode treeRoot;
	private int depth;
	private final ObservableList<TreeNode> children;

	private Path path;

	public TreeNode(
			final ChartData chartData) {
		this(chartData, null);
	}

	public TreeNode(
			final ChartData chartData,
			final TreeNode treeNode) {

		data = chartData;
		parent = treeNode;
		depth = -1;
		children = FXCollections.observableArrayList();
		init();
	}

	private void init() {

		if (null != parent) {
			parent.getChildren().add(this);
		}
	}

	public boolean isRoot() {
		return null == parent;
	}

	public boolean isLeaf() {
		return null == children || children.isEmpty();
	}

	public boolean hasParent() {
		return null != parent;
	}

	public void removeParent() {
		parent = null;
		myRoot = null;
		treeRoot = null;
		depth = -1;
	}

	public TreeNode getParent() {
		return parent;
	}

	public void setParent(
			final TreeNode treeNode) {

		if (null != treeNode) {
			treeNode.addNode(this);
		}
		parent = treeNode;
		myRoot = null;
		treeRoot = null;
		depth = -1;
	}

	public ChartData getData() {
		return data;
	}

	public void setData(
			final ChartData chartData) {
		data = chartData;
	}

	public List<TreeNode> getChildrenUnmodifiable() {
		return Collections.unmodifiableList(children);
	}

	public List<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(
			final List<TreeNode> children) {
		this.children.setAll(new LinkedHashSet<>(children));
	}

	public void addNode(
			final ChartData chartData) {

		final TreeNode child = new TreeNode(chartData);
		child.setParent(this);
		children.add(child);
	}

	public void addNode(
			final TreeNode treeNode) {

		if (!children.contains(treeNode)) {

			treeNode.setParent(this);
			children.add(treeNode);
		}
	}

	public void removeNode(
			final TreeNode treeNode) {
		children.remove(treeNode);
	}

	public void addNodes(
			final TreeNode... treeNodes) {
		addNodes(Arrays.asList(treeNodes));
	}

	public void addNodes(
			final List<TreeNode> treeNodes) {
		treeNodes.forEach(this::addNode);
	}

	public void removeNodes(
			final TreeNode... treeNodes) {
		removeNodes(Arrays.asList(treeNodes));
	}

	public void removeNodes(
			final List<TreeNode> treeNodes) {
		treeNodes.forEach(this::removeNode);
	}

	public void removeAllNodes() {
		children.clear();
	}

	public Stream<TreeNode> stream() {

		final Stream<TreeNode> stream;
		if (isLeaf()) {
			stream = Stream.of(this);
		} else {
			stream = getChildren().stream()
					.map(TreeNode::stream)
					.reduce(Stream.of(this), Stream::concat);
		}
		return stream;
	}

	public Stream<TreeNode> lazyStream() {

		final Stream<TreeNode> stream;
		if (isLeaf()) {
			stream = Stream.of(this);
		} else {
			stream = Stream.concat(Stream.of(this), getChildren().stream().flatMap(TreeNode::stream));
		}
		return stream;
	}

	public Stream<TreeNode> flattened() {
		return Stream.concat(Stream.of(this), children.stream().flatMap(TreeNode::flattened));
	}

	public List<TreeNode> getAll() {
		return flattened().collect(Collectors.toList());
	}

	public List<ChartData> getAllData() {
		return flattened().map(TreeNode::getData).collect(Collectors.toList());
	}

	public int getNoOfNodes() {
		return (int) flattened().count();
	}

	public int getNoOfLeafNodes() {
		return (int) flattened().filter(TreeNode::isLeaf).count();
	}

	public boolean contains(
			final TreeNode treeNode) {
		return flattened().anyMatch(n -> n.equals(treeNode));
	}

	public boolean containsData(
			final ChartData chartData) {
		return flattened().anyMatch(n -> n.data.equals(chartData));
	}

	public TreeNode getMyRoot() {

		if (null == myRoot) {
			if (null != getParent() && getParent().isRoot()) {
				myRoot = this;
			} else {
				myRoot = getMyRoot(getParent());
			}
		}
		return myRoot;
	}

	private static TreeNode getMyRoot(
			final TreeNode treeNode) {

		TreeNode myRoot = null;
		if (treeNode != null) {

			if (treeNode.getParent().isRoot()) {
				myRoot = treeNode;
			} else {
				myRoot = getMyRoot(treeNode.getParent());
			}
		}
		return myRoot;
	}

	public TreeNode getTreeRoot() {

		if (null == treeRoot) {
			if (isRoot()) {
				treeRoot = this;
			} else {
				treeRoot = getTreeRoot(getParent());
			}
		}
		return treeRoot;
	}

	private static TreeNode getTreeRoot(
			final TreeNode treeNode) {

		final TreeNode treeNodeResult;
		if (treeNode.isRoot()) {
			treeNodeResult = treeNode;
		} else {
			treeNodeResult = getTreeRoot(treeNode.getParent());
		}
		return treeNodeResult;
	}

	public int getDepth() {

		if (depth == -1) {
			if (isRoot()) {
				depth = 0;
			} else {
				depth = getDepthRec(getParent(), 0);
			}
		}
		return depth;
	}

	private static int getDepthRec(
			final TreeNode treeNode,
			final int depthParam) {

		final int depthRec;
		int depth = depthParam;
		depth++;
		if (treeNode.isRoot()) {
			depthRec = depth;
		} else {
			depthRec = getDepthRec(treeNode.getParent(), depth);
		}
		return depthRec;
	}

	public int getMaxLevel() {
		return getTreeRoot().stream().map(TreeNode::getDepth).max(Comparator.naturalOrder()).orElse(0);
	}

	public double getPercentageOfTotal() {
		return getPercentageOfTotalRec(this);
	}

	public static double getPercentageOfTotalRec(
			final TreeNode treeNode) {

		final double percentage = treeNode.getPercentage();
		final TreeNode treeNodeParent = treeNode.getParent();
		final double parentPercentage;
		if (treeNodeParent != null) {
			parentPercentage = getPercentageOfTotalRec(treeNodeParent);
		} else {
			parentPercentage = 1.0;
		}
		return percentage * parentPercentage;
	}

	public double getPercentage() {

		final double percentage;
		double sum = 0;
		final List<TreeNode> siblings = getSiblings();
		for (final TreeNode sibling : siblings) {

			final ChartData chartData = sibling.getData();
			sum += chartData.getValue();
		}
		if (Double.compare(sum, 0) == 0) {
			percentage = 1.0;

		} else {
			final ChartData data = getData();
			final double value = data.getValue();
			final double tmpPercentage = value / sum;
			percentage = Math.min(tmpPercentage, 0.999_999);
		}
		return percentage;
	}

	public List<TreeNode> getSiblings() {

		final List<TreeNode> treeNodeList;
		if (null == getParent()) {
			treeNodeList = new ArrayList<>();
		} else {
			treeNodeList = getParent().getChildren();
		}
		return treeNodeList;
	}

	public List<TreeNode> nodesAtSameLevel() {

		final int depth = getDepth();
		return getTreeRoot().stream()
				.filter(node -> node.getDepth() == depth)
				.collect(Collectors.toList());
	}

	public double getParentAngle() {

		final List<TreeNode> parentList = new ArrayList<>();
		TreeNode node = this;
		while (!node.getParent().isRoot()) {
			node = node.getParent();
			parentList.add(node);
		}
		Collections.reverse(parentList);
		double parentAngle = 360.0;
		for (final TreeNode n : parentList) {
			parentAngle = parentAngle * n.getPercentage();
		}
		return parentAngle;
	}

	public void setPath(
			final Path path) {
		this.path = path;
	}

	public Path getPath() {
		return path;
	}
}
