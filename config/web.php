<?php
$HOST_MONGO = getenv("HOST_MONGO_TAG");
$USER_MONGO = getenv("USER_MONGO_TAG");
$PWD_MONGO = getenv("PWD_MONGO_TAG");
$DB_MONGO = getenv("MONGO_TAG");
$PORT_MONGO = getenv("PORT_MONGO_TAG");

$params = require __DIR__ . '/params.php';
$db = require __DIR__ . '/db.php';
$routes = require __DIR__ . '/routes.php';

$config = [
    'id' => 'basic',
    'basePath' => dirname(__DIR__),
    'bootstrap' => ['log'],
    'aliases' => [
        '@bower' => '@vendor/bower-asset',
        '@npm'   => '@vendor/npm-asset',
    ],
    'components' => [
        'request' => [
            // !!! insert a secret key in the following (if it is empty) - this is required by cookie validation
            'cookieValidationKey' => 'zsFuTOnjdH8YqHIDS0LamYdMN_qukCNp',
            'parsers' => [
                'application/json' => 'yii\web\JsonParser',
            ]
        ],
        'mongodb' => [
            'class' => '\yii\mongodb\Connection',
            'dsn' => "mongodb://api-tag:Tq85YyUykssppIUjf2JY8r8TR1jpMgrthQEbD7W7ICzqjuCVW6QTcGsJVc3V6bYxwJAn3dwJwoRQJXGhsYbL7A==@api-tag.mongo.cosmos.azure.com:10255/?ssl=true&replicaSet=globaldb&retrywrites=false&maxIdleTimeMS=120000&appName=@api-tag@"
        ],
        'mysql' => [
            'class' => 'yii\db\Connection',
            'dsn' => 'mysql:host=mysql;dbname=br.ong.tag.santaluzia3',
            'username' => 'root',
            'password' => '10*20$30',
            'charset' => 'utf8',
        ],
        'cache' => [
            'class' => 'yii\caching\FileCache',
        ],
        'user' => [
            'identityClass' => 'app\models\User',
            'enableSession' => false,
            'loginUrl' => null,
        ],
        'errorHandler' => [
            'errorAction' => 'site/error',
        ],
        'mailer' => [
            'class' => 'yii\swiftmailer\Mailer',
            // send all mails to a file by default. You have to set
            // 'useFileTransport' to false and configure a transport
            // for the mailer to send real emails.
            'useFileTransport' => true,
        ],
        'log' => [
            'traceLevel' => YII_DEBUG ? 3 : 0,
            'targets' => [
                [
                    'class' => 'yii\log\FileTarget',
                    'levels' => ['error', 'warning'],
                ],
            ],
        ],
        'db' => $db,
        'urlManager' => [
            'enablePrettyUrl' => true,
            'showScriptName' => false,
            'enableStrictParsing' => false,
            'rules' => $routes,
        ],
    ],
    'modules' => [
        'v1' => [
            'class' => 'app\modules\v1\Module'
        ],
        'migration' => [
            'class' => 'app\modules\migration\Module'
        ],
        'registration' => [
            'class' => 'app\modules\registration\Module'
        ],
        'cras' => [
            'class' => 'app\modules\cras\Module'
        ],
    ],
    'params' => $params,
];

if (YII_ENV_DEV) {
    // configuration adjustments for 'dev' environment
    $config['bootstrap'][] = 'debug';
    $config['modules']['debug'] = [
        'class' => 'yii\debug\Module',
        // uncomment the following to add your IP if you are not connecting from localhost.
        //'allowedIPs' => ['127.0.0.1', '::1'],
    ];

    $config['bootstrap'][] = 'gii';
    $config['modules']['gii'] = [
        'class' => 'yii\gii\Module',
        // uncomment the following to add your IP if you are not connecting from localhost.
        //'allowedIPs' => ['127.0.0.1', '::1'],
    ];
}

return $config;
