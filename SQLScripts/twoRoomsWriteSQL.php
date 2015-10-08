<?php
$currentVersion = 3;//is there some way to make constant?
$version = $_POST["version"];
//TODO When database changes make update depending on version.

//echo 'trying to establish connection' . '<br>';
$db = new mysqli('rdbms.strato.de', '***REMOVED***', '***REMOVED***', '***REMOVED***');

if($db->connect_errno > 0){
	//does die sent the message to the caller?
    die('Unable to connect to database [' . $db->connect_error . ']');
}

$sets = json_decode($_POST["sets"], true);
$maps = array();

$stmtSet = $db->prepare("INSERT INTO sets (name, count, parent, description) VALUES(?,?,?,?)");
$stmtRoles = $db->prepare("INSERT INTO set_role VALUES (?,?)");
foreach ($sets as $set) {
	//check parent
	$parent = $set[3];
	if($parent != -1) {
		$mapping = $maps[$parent];
		if($mapping != null)
			$parent = $mapping;
	}
    $stmtSet->bind_param("siis", $set[1], $set[2], $parent, $set[4]);
	$stmtSet->execute();
	$id = $stmtSet->insert_id;
	$maps[$set[0]] = $id;
	foreach ($set[5] as $role) {
		$stmtRoles->bind_param("ii", $id, $role);
		$stmtRoles->execute();
	}
}
$stmtSet->close();
$stmtRoles->close();

mysqli_close($con);
?>