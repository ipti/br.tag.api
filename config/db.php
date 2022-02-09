<?php
$HOST = getenv("HOST_DB_TAG");
$USER = getenv("USER_DB_TAG");
$PWD = getenv("PWD_DB_TAG");
$DB = getenv("DB_TAG");
return [
    'class' => 'yii\db\Connection',
    'dsn' => "mysql:host=$HOST;dbname=$DB",
    'username' => $USER,
    'password' => $PWD,
    'charset' => 'utf8',

    // Schema cache options (for production environment)
    //'enableSchemaCache' => true,
    //'schemaCacheDuration' => 60,
    //'schemaCache' => 'cache',
];
