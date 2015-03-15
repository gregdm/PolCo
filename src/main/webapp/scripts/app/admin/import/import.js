'use strict';

angular.module('polcoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('import', {
                parent: 'admin',
                url: '/import',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'audits.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/admin/import/import.html',
                        controller: 'ImportController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('import');
                        return $translate.refresh();
                    }]
                }
            });
    });
