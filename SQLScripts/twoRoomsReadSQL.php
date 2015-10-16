<?php
$version = $_GET['v'];//will version be identifed correctly as an int or even double?

if($version < 3) {
	die("Version to small");
}

//echo 'trying to establish connection' . '<br>';
$db = new mysqli('rdbms.strato.de', 'U2285767', 'W3oovyYr56LZt9wIOHUr', 'DB2285767');

if($db->connect_errno > 0){
	//does die sent the message to the caller?
    die('Unable to connect to database [' . $db->connect_error . ']');
}

$since = $_GET['since'];

function echoTable($db, $since, $table, $columns, $intColumns, $resultArray) {
	//echo 'reading table ' . $table . '<br>';
	//echo date("Y-m-d H-i-s" , $since) . '<br>';
	$result = mysqli_query($db, "SELECT " . $columns . " FROM " . $table . " WHERE last_modified >= '" . date("Y-m-d H-i-s" , $since) . "'");
	echo $db->error;
	//echo json_encode($result) . '<br>';
	$tableAsArray = array();

	while($row = mysqli_fetch_row($result)){
		foreach($intColumns as $index) {
			$row[$index] = intval($row[$index]);
		}
		//echo 'Getting row ' . json_encode($row) . '<br>';
		$tableAsArray[] = $row;
	}

	//$result->free(); makes trouble. why?
	//echo 'returning array ' . json_encode($tableAsArray) . '<br>';
	$resultArray[$table] = $tableAsArray;
	return $resultArray;
}
//echo 'started reading tables' . '<br>';

$resultAsArray = array();

$resultAsArray = echoTable($db, $since, 'categories_de', '_id, name', array(0), $resultAsArray);
$resultAsArray = echoTable($db, $since, 'categories_en', '_id, name', array(0), $resultAsArray);
$resultAsArray = echoTable($db, $since, 'teams_de', '_id, name', array(0), $resultAsArray);
$resultAsArray = echoTable($db, $since, 'teams_en', '_id, name', array(0), $resultAsArray);
$resultAsArray = echoTable($db, $since, 'roles_de', '_id, name, description, team_id, `group`, category', array(0, 3, 4, 5), $resultAsArray);
$resultAsArray = echoTable($db, $since, 'roles_en', '_id, name, description, team_id, `group`, category', array(0, 3, 4, 5), $resultAsArray);
$resultAsArray = echoTable($db, $since, 'sets', '_id, name, count, parent, description', array(0, 2, 3), $resultAsArray);
$resultAsArray = echoTable($db, $since, 'set_roles', 'id_set, id_role', array(0, 1), $resultAsArray);

//echo 'finished reading tables' . '<br>';
$resultAsArray['time'] = time();
$json_string = json_encode($resultAsArray);
$length = strlen($json_string);
echo $length . $json_string;
//send the length at the start so the android knows how big the message is.
//static memory allocation is better then dynamic memory allocation

mysqli_close($con);
?>
