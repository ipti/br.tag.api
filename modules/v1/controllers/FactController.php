<?php

namespace app\modules\v1\controllers;

use app\components\AuthController;
use app\modules\v1\models\Fact;
use MongoDB\BSON\ObjectId;
use yii\data\ActiveDataProvider;
use Yii;

class FactController extends AuthController
{

    public function actionIndex()
    {

        $data = Yii::$app->request->get();
        $query = Fact::find();
        $queryCondition = [];


        if(isset($data['createAt'])){

            $createAt = date('Y-m-d', strtotime($data['createAt']));
            $createAt = new DateTime($createAt);
            $createAt = new UTCDateTime($createAt->getTimeStamp());

            $queryCondition['createAt'] = $createAt;
        }

        if(count($queryCondition) > 0){
            $query->where($queryCondition);
        }

        //$query->orderBy('createAt');

        $facts = [];
        $provider = new ActiveDataProvider([
            'query' => $query,
            'pagination' => [
                'pageSize' => 15,
            ]
        ]);

        $pagination = $provider->getPagination();
        $count = $provider->getCount();
        $paginationParam = [];
        if($pagination){
            $paginationParam['currentPage'] = $pagination->page;
            $paginationParam['perPage'] = $pagination->pageSize;
            $paginationParam['totalPages'] = max($pagination->pageCount, 1);
            $paginationParam['totalItens'] = $pagination->totalCount;
        }
        else{
            $paginationParam['currentPage'] = 0;
            $paginationParam['perPage'] = 10;
            $paginationParam['totalPages'] = 0;
            $paginationParam['totalItens'] = $count;
        }

        $models = $provider->getModels();

        foreach($models as $model){
            $facts[] = $model->formatData();
        }
        return array_merge(['facts' => $facts], ['pagination' =>$paginationParam]);
    }

    public function actionCreate()
    {
        $fact = new Fact(['scenario' => Fact::SCENARIO_CREATE]);
        $data['Fact'] = Yii::$app->request->post();

        if ($fact->create($data)) {
            return [
                'status' => '1',
                'payload' => ['_id' => (string) $fact->_id],
                'message' => 'Registro de Fato cadastrado com sucesso'
            ];
        }

        return [
            'status' => '0',
            'error' => $fact->getErrors(),
            'message' => 'Erro ao cadastrar Registro de Fato'
        ];
    }

    public function actionGet($id){
        $fact = Fact::findOne($id);
        if(!is_null($fact)){
            return $fact->formatData();
        }
        return [];
    }

    public function actionUpdate($id)
    {
        $fact = Fact::findOne(new ObjectId($id));
        $fact->scenario = Fact::SCENARIO_UPDATE;
        $data = ['Fact' => Yii::$app->request->post()];

        if ($fact->_update($data)) {
            return [
                'status' => '1',
                'data' => ['_id' => (string) $fact->_id],
                'message' => 'Registro de Fato atualizado com sucesso'
            ];
        }

        return [
            'status' => '0',
            'error' => $fact->getErrors(),
            'message' => 'Erro ao atualizar Registro de Fato'
        ];
    }

    public function actionDelete($id)
    {
        $fact = Fact::findOne(new ObjectId($id));

        if (!is_null($fact) && $fact->delete()) {
            return [
                'status' => '1',
                'message' => 'Registro de Fato excluído com sucesso'
            ];
        }

        return [
            'status' => '0',
            'error' => $fact->getErrors(),
            'message' => 'Erro ao excluir Registro de Fato'
        ];
    }

}

