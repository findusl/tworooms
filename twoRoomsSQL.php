http://codular.com/php-mysqli
<?php 
$db = new mysqli('rdbms.strato.de', '***REMOVED***', '', '***REMOVED***');

if($db->connect_errno > 0){
    die('Unable to connect to database [' . $db->connect_error . ']');
}

$since = $_GET['since'];

$result = mysqli_query($db, "SELECT * FROM categories WHERE last_modified > '$since'");
echo "Table: categories";
echo $result;
$result->free();

$result = mysqli_query($db, "SELECT * FROM roles WHERE last_modified > '$since'");
echo "Table: roles";
echo $result;
$result->free();

$result = mysqli_query($db, "SELECT * FROM sets WHERE last_modified > '$since'");
echo "Table: sets";
echo $result;
$result->free();

$result = mysqli_query($db, "SELECT * FROM set_roles WHERE last_modified > '$since'");
echo "Table: set_roles";
echo $result;
$result->free();

$result = mysqli_query($db, "SELECT * FROM teams WHERE last_modified > '$since'");
echo "Table: teams";
echo $result;
$result->free();

mysqli_close($con);
?>