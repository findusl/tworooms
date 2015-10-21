<?php
$version = $_PUSH["version"];
if($version < 3) {
	die("Version to small");
}
//TODO When database changes make update depending on version.

//echo 'trying to establish connection<br>';
$db = new mysqli('rdbms.strato.de', '*username*', '*pw*', '*db-name*');

if($db->connect_errno > 0){
    die('Unable to connect to database [' . $db->connect_error . ']');
}

//echo "Connected to db. ";

$sets = json_decode($_PUSH["sets"], true);
$maps = array();
//echo "received sets string: ".$_PUSH["sets"]." ";

$stmtSet = $db->prepare("INSERT INTO sets (name, count, parent, description) VALUES(?,?,?,?)");
$stmtRoles = $db->prepare("INSERT INTO set_roles (id_set, id_role) VALUES (?,?)");
foreach ($sets as $set) {
	//echo 'writing set: '.json_encode($set).' ';
	//check parent
	$parent = $set[3];
	if($parent != -1) {
		$mapping = $maps[$parent];
		if($mapping != null)
			$parent = $mapping;
	}
    $stmtSet->bind_param("siis", $set[1], $set[2], $parent, $set[4]);
	$stmtSet->execute();
	echo $db->error;
	$id = $stmtSet->insert_id;
	$maps[$set[0]] = $id;
	foreach ($set[5] as $role) {
		//echo 'role: '.$role.' ';
		$stmtRoles->bind_param("ii", $id, $role);
		$stmtRoles->execute();
		echo $db->error;
	}
}
$stmtSet->close();
$stmtRoles->close();

mysqli_close($con);
?>