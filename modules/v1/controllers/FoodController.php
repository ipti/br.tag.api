<?php

namespace app\modules\v1\controllers;

use app\components\AuthController;
use app\modules\v1\models\Food;
use MongoDB\BSON\ObjectId;
use yii\data\ActiveDataProvider;
use Yii;

class FoodController extends AuthController
{

    public function actionIndex()
    {

        $data = Yii::$app->request->get();
        $query = Food::find();
        $queryCondition = [];

        if(isset($data['personApplicant'])){
            $queryCondition['personApplicant'] = ['$regex' => '/^'.$data['personApplicant'].'/'];
        }

        if(isset($data['personRepresentative'])){
            $queryCondition['personRepresentative'] = ['$regex' => '/^' . $data['personRepresentative'] . '/'];
        }

        if(isset($data['personRequired'])){
            $queryCondition['personRequired'] = ['$regex' => '/^' . $data['personRequired'] . '/'];
        }

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

        $foods = [];
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
            $foods[] = $model->formatData();
        }
        return array_merge(['foods' => $foods], ['pagination' =>$paginationParam]);
    }

    public function actionCreate()
    {
        $food = new Food(['scenario' => Food::SCENARIO_CREATE]);
        $data['Food'] = Yii::$app->request->post();

        if ($food->create($data)) {
            return [
                'status' => '1',
                'payload' => ['_id' => (string) $food->_id],
                'message' => 'Ação de alimentos cadastrada com sucesso'
            ];
        }

        return [
            'status' => '0',
            'error' => $food->getErrors(),
            'message' => 'Erro ao cadastrar ação de alimentos'
        ];
    }

    public function actionGet($id){
        $food = Food::findOne($id);
        if(!is_null($food)){
            return $food->formatData();
        }
        return [];
    }

    public function actionUpdate($id)
    {
        $food = Food::findOne(new ObjectId($id));
        $food->scenario = Food::SCENARIO_UPDATE;
        $data = ['Food' => Yii::$app->request->post()];

        if ($food->_update($data)) {
            return [
                'status' => '1',
                'data' => ['_id' => (string) $food->_id],
                'message' => 'Ação de alimentos atualizada com sucesso'
            ];
        }

        return [
            'status' => '0',
            'error' => $food->getErrors(),
            'message' => 'Erro ao atualizar ação de alimentos'
        ];
    }

    public function actionDelete($id)
    {
        $food = Food::findOne(new ObjectId($id));

        if (!is_null($food) && $food->delete()) {
            return [
                'status' => '1',
                'message' => 'Ação de alimentos excluída com sucesso'
            ];
        }

        return [
            'status' => '0',
            'error' => $food->getErrors(),
            'message' => 'Erro ao excluir ação de alimentos'
        ];
    }

}

