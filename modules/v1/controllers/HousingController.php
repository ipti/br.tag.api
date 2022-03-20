<?php

namespace app\modules\v1\controllers;

use app\components\AuthController;
use app\modules\v1\models\Housing;
use MongoDB\BSON\ObjectId;
use yii\data\ActiveDataProvider;
use Yii;

class HousingController extends AuthController
{

    public function actionIndex()
    {

        $data = Yii::$app->request->get();
        $query = Housing::find();
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

        $housings = [];
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
            $housings[] = $model->formatData();
        }
        return array_merge(['housings' => $housings], ['pagination' =>$paginationParam]);
    }

    public function actionCreate()
    {
        $housing = new Housing(['scenario' => Housing::SCENARIO_CREATE]);
        $data['Housing'] = Yii::$app->request->post();

        if ($housing->create($data)) {
            return [
                'status' => '1',
                'payload' => ['_id' => (string) $housing->_id],
                'message' => 'Termo de Abrigamento cadastrada com sucesso'
            ];
        }

        return [
            'status' => '0',
            'error' => $housing->getErrors(),
            'message' => 'Erro ao cadastrar Termo de Abrigamento'
        ];
    }

    public function actionGet($id){
        $housing = Housing::findOne($id);
        if(!is_null($housing)){
            return $housing->formatData();
        }
        return [];
    }

    public function actionUpdate($id)
    {
        $housing = Housing::findOne(new ObjectId($id));
        $housing->scenario = Housing::SCENARIO_UPDATE;
        $data = ['Housing' => Yii::$app->request->post()];

        if ($housing->_update($data)) {
            return [
                'status' => '1',
                'data' => ['_id' => (string) $housing->_id],
                'message' => 'Termo de Abrigamento atualizada com sucesso'
            ];
        }

        return [
            'status' => '0',
            'error' => $housing->getErrors(),
            'message' => 'Erro ao atualizar Termo de Abrigamento'
        ];
    }

    public function actionDelete($id)
    {
        $housing = Housing::findOne(new ObjectId($id));

        if (!is_null($housing) && $housing->delete()) {
            return [
                'status' => '1',
                'message' => 'Termo de Abrigamento excluído com sucesso'
            ];
        }

        return [
            'status' => '0',
            'error' => $housing->getErrors(),
            'message' => 'Erro ao excluir Termo de Abrigamento'
        ];
    }

}

