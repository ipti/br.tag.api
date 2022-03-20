<?php

namespace app\modules\v1\controllers;

use app\components\AuthController;
use app\modules\v1\models\Warning;
use MongoDB\BSON\ObjectId;
use yii\data\ActiveDataProvider;
use Yii;

class WarningController extends AuthController
{

    public function actionIndex()
    {

        $data = Yii::$app->request->get();
        $query = Warning::find();
        $queryCondition = [];

        if(isset($data['personAdolescent'])){
            $queryCondition['personAdolescent'] = ['$regex' => '/^'.$data['personAdolescent'].'/'];
        }

        if(isset($data['personRepresentative'])){
            $queryCondition['personRepresentative'] = ['$regex' => '/^' . $data['personRepresentative'] . '/'];
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

        $warnings = [];
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
            $warnings[] = $model->formatData();
        }
        return array_merge(['warnings' => $warnings], ['pagination' =>$paginationParam]);
    }

    public function actionCreate()
    {
        $warning = new Warning(['scenario' => Warning::SCENARIO_CREATE]);
        $data['Warning'] = Yii::$app->request->post();

        if ($warning->create($data)) {
            return [
                'status' => '1',
                'payload' => ['_id' => (string) $warning->_id],
                'message' => 'Advertência cadastrada com sucesso'
            ];
        }

        return [
            'status' => '0',
            'error' => $warning->getErrors(),
            'message' => 'Erro ao cadastrar advertência'
        ];
    }

    public function actionGet($id){
        $warning = Warning::findOne($id);
        if(!is_null($warning)){
            return $warning->formatData();
        }
        return [];
    }

    public function actionUpdate($id)
    {
        $warning = Warning::findOne(new ObjectId($id));
        $warning->scenario = Warning::SCENARIO_UPDATE;
        $data = ['Warning' => Yii::$app->request->post()];

        if ($warning->_update($data)) {
            return [
                'status' => '1',
                'data' => ['_id' => (string) $warning->_id],
                'message' => 'Advertência atualizada com sucesso'
            ];
        }

        return [
            'status' => '0',
            'error' => $warning->getErrors(),
            'message' => 'Erro ao atualizar advertência'
        ];
    }

    public function actionDelete($id)
    {
        $warning = Warning::findOne(new ObjectId($id));

        if (!is_null($warning) && $warning->delete()) {
            return [
                'status' => '1',
                'message' => 'Advertência excluída com sucesso'
            ];
        }

        return [
            'status' => '0',
            'error' => $warning->getErrors(),
            'message' => 'Erro ao excluir advertência'
        ];
    }

}

