<?php
$currentVersion = 3;//is there some way to make constant?
$version = $_POST["version"];
//TODO When database changes make update depending on version.
$sets = json_decode($_POST["sets"], true);
$maps = array();
foreach ($sets as $set) {
	//check parent
	$parent = $set["parent"];
	if($parent != -1) {
		$mapping = $maps[$parent];
		if($mapping != null)
			$parent = $mapping;
	}
    $id = "INSERT INTO sets (name, description, count, parent) VALUES(".$set["name"].",".$set["description"].",".$set["count"].",".$parent.")";
	$maps[$set["id"]] = $id;
	foreach ($set["roles"] as $role) {
		"INSERT INTO set_role VALUES (".$id.",".$role")";
	}
}
?>