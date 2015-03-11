'use strict';

angular.module('polcoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('expression', {
                parent: 'entity',
                url: '/expression',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'polcoApp.expression.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/expression/expressions.html',
                        controller: 'ExpressionController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('expression');
                        return $translate.refresh();
                    }]
                }
            })
            .state('expressionDetail', {
                parent: 'entity',
                url: '/expression/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'polcoApp.expression.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/expression/expression-detail.html',
                        controller: 'ExpressionDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('expression');
                        return $translate.refresh();
                    }]
                }
            });
    });
