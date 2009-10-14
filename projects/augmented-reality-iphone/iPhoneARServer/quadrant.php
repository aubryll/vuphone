<?php
include_once("configuration.php");
include_once("objects/class.database.php");
include_once("objects/class.poi.php");
include_once("xml/Tree.php");
date_default_timezone_set('America/Chicago');

// figure out what the user is requesting
$x = $_REQUEST["x"];
$y = $_REQUEST["y"];
$format = $_REQUEST["format"];

if ($format == "")
	$format = "xml";
	
if ($format != "xml"){
	echo "Only XML format is supported right now.";
	die();
}

// instantiate object
$tree = new XML_Tree();

// add the root element
$root =& $tree->addRoot("POICollection");
$root->setAttribute("x", $x);
$root->setAttribute("y", $y);
$root->setAttribute("timestamp", time());

// fetch poi child elements
$terms = array(array("quadrant_x", "=", $x), array("quadrant_y", "=", $y));
$handle = new POI();
$pois = $handle->GetList($terms, '', true, 1000);

foreach ($pois as $poi){
	$node = &$root->addChild("POI"); 
	$node->setAttribute("name", $poi->title);
	$node->setAttribute("lat", $poi->lat);
	$node->setAttribute("lon", $poi->lon);
	$node->addChild("description", $poi->description);
	$node->addChild("url", $poi->url);
}

// print tree
$tree->dump();
?>