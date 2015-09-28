<?php
$currentVersion = 1.0;//is there some way to make constant?
$version = $_GET['v'];//will version be identifed correctly as an int or even double?

if($version < $currentVersion) {
	//does die sent the message to the caller?
    die('Version is to small. Needs at least version ' . $currentVersion . '. Please update your app');
}

//echo 'trying to establish connection' . '<br>';
$db = new mysqli('rdbms.strato.de', 'U2285767', 'W3oovyYr56LZt9wIOHUr', 'DB2285767');

if($db->connect_errno > 0){
	//does die sent the message to the caller?
    die('Unable to connect to database [' . $db->connect_error . ']');
}

$since = $_GET['since'];

function echoTable($db, $since, $table, $columns, $intColumns) {
	//echo 'reading table ' . $table . '<br>';
	//echo date("Y-m-d H-i-s" , $since) . '<br>';
	$result = mysqli_query($db, "SELECT " . $columns . " FROM " . $table . " WHERE last_modified >= '" . date("Y-m-d H-i-s" , $since) . "'");

	//echo json_encode($result) . '<br>';
	$tableAsArray = array();

	while($row = mysqli_fetch_row($result)){
		foreach($intColumns as $index) {
			$row[$index] = intval($row[$index]);
		}
		//echo 'Getting row ' . json_encode($row) . '<br>';
		//echo 'example from the row: ' . $row[1];
		$tableAsArray[] = $row;
	}

	//$result->free(); makes trouble. why?
	//echo 'returning array ' . json_encode($tableAsArray) . '<br>';
	return $tableAsArray;
}
//echo 'started reading tables' . '<br>';



$resultAsArray = array();

$resultAsArray['categories'] = echoTable($db, $since, 'categories', '_id, name', array(0));
$resultAsArray['roles'] = echoTable($db, $since, 'roles', '_id, name, description, team_id, groupe, category', array(0, 3, 4, 5));
$resultAsArray['sets'] = echoTable($db, $since, 'sets', '_id, name, count, parent, description', array(0, 2, 3));
$resultAsArray['set_roles'] = echoTable($db, $since, 'set_roles', 'id_set, id_role', array(0, 1));
$resultAsArray['teams'] = echoTable($db, $since, 'teams', '_id, name', array(0));

//echo 'finished reading tables' . '<br>';

$json_string = 'time:' . time() . json_encode($resultAsArray);
$length = strlen($json_string);
echo $length . $json_string;
//echo the length at the start so the android knows how big the array has to be.
//static memory allocation is better then dynamic memory allocation

mysqli_close($con);
?>