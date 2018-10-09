<?php

namespace app\modules\v1\controllers;

use app\components\AuthController;
use app\modules\v1\models\User;
use MongoDB\BSON\ObjectId;
use yii\data\ActiveDataProvider;
use Yii;

class UserController extends AuthController
{

    public static function allowedDomains() {
        return [
            '*'
        ];
    }
    
    /**
     * @inheritdoc
     */
    public function behaviors() {
        return array_merge(parent::behaviors(), [
    
            // For cross-domain AJAX request
            'corsFilter'  => [
                'class' => \yii\filters\Cors::className(),
                'cors'  => [
                    // restrict access to domains:
                    'Origin'                           => static::allowedDomains(),
                    'Access-Control-Request-Method'    => ['POST','OPTIONS','GET','PUT','HEAD','DELETE','PATCH'],
                    'Access-Control-Request-Headers'    => ['*'],
                    'Access-Control-Allow-Credentials' => true,
                    'Access-Control-Max-Age'           => 3600,                 // Cache (seconds)
                ],
            ],
    
        ]);
    }

    public function actionIndex()
    {
        return new ActiveDataProvider([
            'query' => User::find(),
            'pagination' => [
                'pageSize' => 10,
            ]
        ]);
    }
    
    public function actionView($id)
    {
        return User::findOne(new ObjectId($id));
    }
    
    public function actionCreate()
    {
        $user = new User;
        $data['User'] = Yii::$app->request->post();

        if ($user->load($data) && $user->save()) {
            return [
                'status' => '1',
                'data' => ['_id' => (string) $user->_id],
                'message' => 'usuário cadastrado com sucesso'
            ];
        }

        return [
            'status' => '0',
            'error' => $user->getErrors(),
            'message' => 'Erro ao cadastrar usuário'
        ];
    }
    
    public function actionUpdate($id)
    {
        $user = User::findOne(new ObjectId($id));
        $data = ['User' => Yii::$app->request->post()];

        if ($user->load($data) && $user->save()) {
            return [
                'status' => '1',
                'data' => ['_id' => (string) $user->_id],
                'message' => 'usuário atualizado com sucesso'
            ];
        }

        return [
            'status' => '0',
            'error' => $user->getErrors(),
            'message' => 'Erro ao atualizar usuário'
        ];
    }
    
    public function actionDelete($id)
    {
        $user = User::findOne(new ObjectId($id));
        $data = ['User' => Yii::$app->request->post()];

        if ($user !== null && $user->delete()) {
            return [
                'status' => '1',
                'data' => ['_id' => $id],
                'message' => 'usuário excluído com sucesso'
            ];
        }

        return [
            'status' => '0',
            'error' => $user->getErrors(),
            'message' => 'Erro ao excluir usuário'
        ];
    }
}



?>
