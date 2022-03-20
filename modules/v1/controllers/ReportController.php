<?php

namespace app\modules\v1\controllers;

use app\components\AuthController;
use app\modules\v1\models\Report;
use MongoDB\BSON\ObjectId;
use yii\data\ActiveDataProvider;
use Yii;

class ReportController extends AuthController
{

    public function actionIndex()
    {

        $data = Yii::$app->request->get();
        $query = Report::find();
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

        $reports = [];
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
            $reports[] = $model->formatData();
        }
        return array_merge(['reports' => $reports], ['pagination' =>$paginationParam]);
    }

    public function actionCreate()
    {
        $report = new Report(['scenario' => Report::SCENARIO_CREATE]);
        $data['Report'] = Yii::$app->request->post();

        if ($report->create($data)) {
            return [
                'status' => '1',
                'payload' => ['_id' => (string) $report->_id],
                'message' => 'Relatório cadastrado com sucesso'
            ];
        }

        return [
            'status' => '0',
            'error' => $report->getErrors(),
            'message' => 'Erro ao cadastrar relatório'
        ];
    }

    public function actionGet($id){
        $report = Report::findOne($id);
        if(!is_null($report)){
            return $report->formatData();
        }
        return [];
    }

    public function actionUpdate($id)
    {
        $report = Report::findOne(new ObjectId($id));
        $report->scenario = Report::SCENARIO_UPDATE;
        $data = ['Report' => Yii::$app->request->post()];

        if ($report->_update($data)) {
            return [
                'status' => '1',
                'data' => ['_id' => (string) $report->_id],
                'message' => 'Relatório atualizado com sucesso'
            ];
        }

        return [
            'status' => '0',
            'error' => $report->getErrors(),
            'message' => 'Erro ao atualizar relatório'
        ];
    }

    public function actionDelete($id)
    {
        $report = Report::findOne(new ObjectId($id));


        if (!is_null($report) && $report->delete()) {
            return [
                'status' => '1',
                'message' => 'Relatório excluída com sucesso'
            ];
        }

        return [
            'status' => '0',
            'error' => $report->getErrors(),
            'message' => 'Erro ao atualizar relatório'
        ];
    }

}

