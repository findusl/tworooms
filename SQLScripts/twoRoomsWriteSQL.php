<?php
$currentVersion = 3;//is there some way to make constant?
$version = $_POST["version"];
//TODO When database changes make update depending on version.
$sets = json_decode($_POST["sets"], true);
$maps = array();
foreach ($sets as $set) {
	//check parent
	$parent = $set[3];
	if($parent != -1) {
		$mapping = $maps[$parent];
		if($mapping != null)
			$parent = $mapping;
	}
    $id = "INSERT INTO sets (name, count, parent, description) VALUES(".$set[1].",".$set[2].",".$set[3].",".$parent.")";
	$maps[$set[0]] = $id;
	foreach ($set[5] as $role) {
		"INSERT INTO set_role VALUES (".$id.",".$role")";
	}
}
?>