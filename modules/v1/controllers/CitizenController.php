<?php

namespace app\modules\v1\controllers;

use app\components\AuthController;
use app\modules\v1\models\Complaint;
use MongoDB\BSON\ObjectId;
use yii\data\ActiveDataProvider;
use yii\web\UploadedFile;
use Yii;

class CitizenController extends AuthController
{

    public function actionIndex()
    {
        return new ActiveDataProvider([
            'query' => Complaint::find(),
            'pagination' => [
                'pageSize' => 10,
            ]
        ]);
    }
    
    public function actionView($id)
    {
        $complaint = Complaint::findOne(new ObjectId($id));

        if(!is_null($complaint)){
            return [
                'status' => '1',
                'data' => $complaint->getAttributes(),
                'message' => 'Denúncia carregada com sucesso'
            ];
        }

        return [
            'status' => '0',
            'error' => ['complaint' => 'Denúncia não encontrada'],
            'message' => 'Denúncia não encontrada'
        ];
    }
    
    public function actionCreate()
    {
        $complaint = new Complaint(['scenario' => Complaint::SCENARIO_CITIZEN]);
        $data['Complaint'] = Yii::$app->request->post();

        if(isset($data['Complaint']['forwards'])){

            if ($complaint->create($data)) {
                return [
                    'status' => '1',
                    'data' => ['_id' => (string) $complaint->_id],
                    'message' => 'Denúncia cadastrada com sucesso'
                ];
            }
        }

        return [
            'status' => '0',
            'error' => $complaint->getErrors(),
            'message' => 'Erro ao cadastrar denúncia'
        ];
    }

    public function actionOptions(){
        return[];
    }

}



?>
