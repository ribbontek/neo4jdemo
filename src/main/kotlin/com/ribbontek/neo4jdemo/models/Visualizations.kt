package com.ribbontek.neo4jdemo.models

data class Visualization(
    val nodes: Collection<VisualizationNode>,
    val relationships: Collection<VisualizationRelationship>
)

data class VisualizationNode(
    val labels: Iterable<String>,
    val properties: Map<String, Any>
)

data class VisualizationRelationship(
    val type: String,
    var properties: Map<String, Any>
)
